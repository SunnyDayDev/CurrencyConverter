package me.sunnydaydev.curencyconverter.converter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.Bindable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import me.sunnydaydev.curencyconverter.coregeneral.rx.defaultSchedulers
import me.sunnydaydev.curencyconverter.coregeneral.rx.retryWithDelay
import me.sunnydaydev.curencyconverter.coreui.binding.observable.Command
import me.sunnydaydev.curencyconverter.coreui.binding.observable.SwapableObservableList
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.curencyconverter.coreui.viewModel.bindable
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.curencyconverter.domain.currencies.CurrencyRates
import me.sunnydaydev.modernrx.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 *
 * Contain business logic which interact within module.
 *
 */

internal class ConverterViewModel @Inject constructor(
        private val interactor: ConverterInteractor,
        private val itemViewModelFactory: CurrencyItemViewModel.Factory,
        private val core: ConverterViewModel.Core
): BaseVewModel() {

    @get:Bindable var currencies by bindable(SwapableObservableList<CurrencyItemViewModel>())

    val scrollToPositionCommand = Command<Int>()

    private val startedScopeDisposable = DisposableBag()

    private var started = false

    init {

        core.base
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeIt { base ->
                    val baseItem = currencies.find { it.code == base } ?: return@subscribeIt
                    scrollToPositionCommand.fire(0)
                    currencies.move(
                            fromIndex = currencies.indexOf(baseItem),
                            toIndex = 0
                    )
                }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onViewStart() {

        started = true

        if (currencies.isEmpty()) {
            loadCurrencies()
        } else {
            startCheckRates()
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onViewStop() {
        started = false
        startedScopeDisposable.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        this.currencies.forEach { it.clear() }
    }

    private fun loadCurrencies() {
        interactor.getCurrencies()
                .defaultSchedulers()
                .subscribeIt(onSuccess = ::handleCurrencies)
    }

    private fun handleCurrencies(currencies: List<Currency>) {

        val baseCurrency = currencies.find { it.code == "EUR" } ?: currencies.first()

        val sorted = currencies.toMutableList()
        sorted.remove(baseCurrency)
        sorted.add(0, baseCurrency)

        this.currencies.forEach { it.clear() }
        this.currencies = SwapableObservableList<CurrencyItemViewModel>()
                .apply { addAll(sorted.map(itemViewModelFactory::create)) }

        this.currencies.firstOrNull()?.focused = true

        core.setBase(baseCurrency.code, 1.0)

        startCheckRates()

    }

    private fun startCheckRates() {

        if (!started) return

        // Processing flag for skipping timer event while request processing
        var processing = false

        val interval = Observable.interval(1, TimeUnit.SECONDS)
                .startWith(-1)
                .filter { !processing }

        Observables.combineLatest(interval, core.base) { _, base -> base }
                .flatMapSingle { base ->
                    processing = true
                    interactor.getCurrencyRates(base)
                            .doFinally { processing = false }
                }
                .disposeBy(startedScopeDisposable)
                .defaultSchedulers()
                .retryWithDelay(500, TimeUnit.MILLISECONDS)
                .subscribeIt(
                        onNext = core::setRates,
                        onError = SimpleErrorHandler(true, ::handleCurrenciesError)
                )

    }

    private fun handleCurrenciesError(error: Throwable) {
        error.printStackTrace()
    }

    @Singleton
    class Core @Inject constructor(){

        private val baseSubject = BehaviorSubject.create<String>()
        private val baseAmountSubject = BehaviorSubject.create<Double>()
        private val ratesSubject = BehaviorSubject.create<CurrencyRates>()

        val base: Observable<String> = baseSubject.distinctUntilChanged()
        val baseAmount: Observable<Double> = baseAmountSubject.distinctUntilChanged()
        val rates: Observable<Map<String, Double>> by lazy { checkedRates() }

        fun setBase(code: String, amount: Double) {

            baseSubject.onNext(code)
            baseAmountSubject.onNext(amount)

        }

        fun setRates(rates: CurrencyRates) {
            ratesSubject.onNext(rates)
        }

        private fun checkedRates(): Observable<Map<String, Double>> {
            return Observables.combineLatest(base, ratesSubject)
                    .map { (base, rates) ->
                        if (base == rates.base) rates.rates + (base to 1.0)
                        else fallbackRates(base, rates)
                    }
        }

        private fun fallbackRates(base: String, ratesSource: CurrencyRates): Map<String, Double> {

            val fallbackRatio = 1 / ratesSource.rates[base]!!
            val fallbackValues = mapOf(base to 1.0, ratesSource.base to fallbackRatio)
            return ratesSource.rates
                    .mapValues { it.value * fallbackRatio } + fallbackValues

        }

    }

}