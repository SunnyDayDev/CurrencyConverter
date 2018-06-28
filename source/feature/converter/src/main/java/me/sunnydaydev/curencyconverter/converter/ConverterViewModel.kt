package me.sunnydaydev.curencyconverter.converter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.databinding.Bindable
import android.os.SystemClock
import androidx.lifecycle.LifecycleObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import me.sunnydaydev.curencyconverter.coregeneral.rx.defaultSchedulers
import me.sunnydaydev.curencyconverter.coregeneral.rx.retryWithDelay
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.curencyconverter.coreui.viewModel.LifecycleViewModel
import me.sunnydaydev.curencyconverter.coreui.viewModel.ViewModelState
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.curencyconverter.domain.currencies.CurrencyRates
import me.sunnydaydev.modernrx.*
import me.sunnydaydev.mvvmkit.observable.Command
import me.sunnydaydev.mvvmkit.observable.MVVMArrayList
import me.sunnydaydev.mvvmkit.observable.MVVMList
import me.sunnydaydev.mvvmkit.observable.bindable
import me.sunnydaydev.mvvmkit.util.ViewLifeCycle
import java.text.SimpleDateFormat
import java.util.*
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
        private val core: ConverterViewModel.Core,
        viewLifeCycle: ViewLifeCycle
): LifecycleViewModel(viewLifeCycle), LifecycleObserver {

    companion object {

        val TIME_FORMATTER get() = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    }

    @get:Bindable var currencies: MVVMList<CurrencyItemViewModel> by bindable(MVVMArrayList())

    @get:Bindable var state by bindable(ViewModelState.LOADING)

    @get:Bindable var connectionLost by bindable(false)

    @get:Bindable var connectionLostTime: String? by bindable(null)

    val scrollToPositionCommand = Command<Int>()

    private var connectionLostStartTime: Long = 0

    private val startedScopeDisposable = DisposableBag()
    private val currenciesRateErrorDisposable = OptionalDisposable()

    init {

        core.base
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeIt { base ->
                    val baseItem = currencies.find { it.code == base } ?: return@subscribeIt
                    scrollToPositionCommand.fire(0)
                    orderItem(0, baseItem)
                }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onViewStart() {

        startedScopeDisposable.enabled = true

        if (currencies.isEmpty()) {
            loadCurrencies()
        } else {
            startCheckRates()
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onViewStop() {

        startedScopeDisposable.enabled = false

        val knownOrder = currencies.map { it.code }
        interactor.storeKnownOrder(knownOrder)
                .defaultSchedulers()
                .subscribeIt()

    }

    fun onRetryClicked() {
        loadCurrencies()
    }

    fun onItemMoved(from: Int, to: Int): Boolean {

        if (from == 0) return false

        val vm = currencies[from]
        orderItem(to, vm)

        return true

    }

    fun canDropOverItem(current:Int, target: Int): Boolean = target != 0 && current != 0

    override fun onCleared() {
        super.onCleared()
        this.currencies.forEach { it.clear() }
    }

    private fun loadCurrencies() {

        state = ViewModelState.LOADING

        Singles.zip(interactor.getCurrencies(), interactor.getKnownOrder())
                .defaultSchedulers()
                .subscribeIt(
                        onSuccess = { (currencies, order) -> handleCurrencies(currencies, order) },
                        onError = SimpleErrorHandler(false) { _ ->
                            state = ViewModelState.ERROR
                        }
                )

    }

    private fun handleCurrencies(currencies: List<Currency>, knownOrder: List<String>) {

        val initialBase = knownOrder.firstOrNull() ?: "EUR"

        val baseCurrency = currencies.find { it.code == initialBase } ?: currencies.first()

        val sortedCurrencies = currencies.toMutableList()
        sortedCurrencies.remove(baseCurrency)
        sortedCurrencies.add(0, baseCurrency)

        sortedCurrencies.sortBy {
            knownOrder.indexOf(it.code).takeIf { it != -1 } ?: Int.MAX_VALUE
        }

        this.currencies.forEach { it.clear() }
        this.currencies = MVVMArrayList<CurrencyItemViewModel>().apply {
              sortedCurrencies.map {
                  itemViewModelFactory.create(it) { orderItem(1, it) }
              } .also { addAll(it) }
        }

        this.currencies.firstOrNull()?.focused = true

        core.setBase(baseCurrency.code, 1.0)

        startCheckRates()

        state = ViewModelState.CONTENT

    }

    private fun startCheckRates() {

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
                            .subscribeOn(Schedulers.io())
                }
                .disposeBy(startedScopeDisposable)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { onCurrenciesRatesError() }
                .retryWithDelay(500, TimeUnit.MILLISECONDS)
                .doOnNext { onCurrenciesErrorResolved() }
                .subscribeIt(onNext = core::setRates)

    }

    private fun onCurrenciesRatesError() {

        if (connectionLost) {
            return
        }

        connectionLostTime = "00:00:00"
        connectionLost = true

        connectionLostStartTime = SystemClock.elapsedRealtime()

        Observable.interval(200, TimeUnit.MILLISECONDS)
                .defaultSchedulers()
                .subscribeIt {

                    val diff = SystemClock.elapsedRealtime() - connectionLostStartTime
                    connectionLostTime = TIME_FORMATTER.format(Date(diff))

                }
                .disposeBy(currenciesRateErrorDisposable)

    }

    private fun orderItem(position: Int, vm: CurrencyItemViewModel) {

        val currentIndex = currencies.indexOf(vm)
        if (currentIndex == position || currentIndex == -1) return

        currencies.move(
                fromIndex = currentIndex,
                toIndex = position
        )

    }

    private fun onCurrenciesErrorResolved() {
        currenciesRateErrorDisposable.dispose()
        connectionLost = false
        connectionLostTime = null
    }

    @Singleton
    class Core @Inject constructor(){

        private val baseSubject = BehaviorSubject.create<String>()
        private val baseAmountSubject = BehaviorSubject.create<Double>()
        private val ratesSubject = BehaviorSubject.create<CurrencyRates>()

        val base: Observable<String> = baseSubject.distinctUntilChanged()
        private val baseAmount: Observable<Double> = baseAmountSubject.distinctUntilChanged()
        private val rates: Observable<Map<String, Double>> by lazy { checkedRates() }

        fun setBase(code: String, amount: Double) {

            baseSubject.onNext(code)
            baseAmountSubject.onNext(amount)

        }

        fun setRates(rates: CurrencyRates) {
            ratesSubject.onNext(rates)
        }

        fun getAmount(currencyCode: String): Observable<Pair<Boolean, Double>> {

            val amountSource = Observables.combineLatest(baseAmount, rates) { base, rates ->
                val nonNullBase = if (base == 0.0) 1.0 else base
                rates.getOrElse(currencyCode) { 0.0 } * nonNullBase
            }

            return base.switchMap {
                val isBase = currencyCode == it
                val source = amountSource.map { isBase to it }
                if (!isBase)  source
                else source.firstElement().toObservable()
                        .concatWith(Observable.never())
            }

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