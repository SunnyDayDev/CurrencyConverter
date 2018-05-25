package me.sunnydaydev.curencyconverter.converter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.Bindable
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import me.sunnydaydev.curencyconverter.coregeneral.rx.defaultSchedulers
import me.sunnydaydev.curencyconverter.coregeneral.rx.retryWithDelay
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.curencyconverter.coreui.viewModel.bindable
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.curencyconverter.domain.currencies.CurrencyRate
import me.sunnydaydev.modernrx.ModernRxSubscriber
import me.sunnydaydev.modernrx.OptionalDisposable
import me.sunnydaydev.modernrx.SuccessErrorHandler
import me.sunnydaydev.modernrx.disposeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 *
 * Contain business logic which interact within module.
 *
 */

internal class ConverterViewModel @Inject constructor(
        private val interactor: ConverterInteractor,
        modernRxHandler: ModernRxSubscriber.Handler
): BaseVewModel(modernRxHandler) {

    @get:Bindable var test by bindable("Loading...")

    private var base = BehaviorSubject.createDefault(Currency.EUR)

    private val currencyUpdateDisposable = OptionalDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onViewStart() {
        startCheckCurrencies()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onViewStop() {
        currencyUpdateDisposable.disposeAndClear()
    }

    private fun startCheckCurrencies() {

        // Processing flag for skipping timer event while request processing
        var processing = false

        val interval = Observable.interval(1, TimeUnit.SECONDS)
                .startWith(-1)
                .filter { !processing }

        Observables.combineLatest(interval, base) { _, base -> base }
                .flatMapSingle { base ->
                    processing = true
                    interactor.getCurrencyRates(base)
                            .map { base to it }
                            .doFinally { processing = false }
                }
                .disposeBy(currencyUpdateDisposable)
                .defaultSchedulers()
                .retryWithDelay(500, TimeUnit.MILLISECONDS)
                .subscribeIt(
                        onNext = { handleRates(it.first, it.second) },
                        onError = SuccessErrorHandler(::handleCurrenciesError)
                )

    }

    private fun handleRates(base: Currency, rates: List<CurrencyRate>) {

        val ratesString = rates.joinToString(separator = "\n") {
            "${it.currency.alpha3code}: ${it.rate}"
        }

        test = "Rates for: ${base.alpha3code}\n$ratesString"

    }

    private fun handleCurrenciesError(error: Throwable) {
        test = "Error:\n${error.localizedMessage}"
    }

}