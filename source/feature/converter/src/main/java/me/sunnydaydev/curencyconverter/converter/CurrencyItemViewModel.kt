package me.sunnydaydev.curencyconverter.converter

import android.databinding.Bindable
import com.github.nitrico.lastadapter.StableId
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.curencyconverter.coreui.viewModel.bindable
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.modernrx.ModernRxSubscriber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal class CurrencyItemViewModel(
        private val stableIdProvider: StableIdProvider,
        private val currency: Currency,
        private val core: ConverterViewModel.Core,
        override val modernRxHandler: ModernRxSubscriber.Handler
): BaseVewModel(), StableId {

    override val stableId: Long get() = stableIdProvider[currency.code]

    @get:Bindable var amount by bindable("") { notifyChanged() }

    @get:Bindable val code by bindable(currency.code)
    @get:Bindable val description by bindable(currency.name)
    @get:Bindable val flag by bindable(currency.flagUrl)
    @get:Bindable var focused by bindable(false, onChange = ::onFocusChanged)

    private val disposable: Disposable
    private var isBase = false

    init {

        disposable = core.base
                .doOnNext { isBase = currency.code == it }
                .filter { currency.code != it }
                .flatMap {
                    Observables.combineLatest(core.baseAmount, core.rates) { base, rates ->
                        val nonNullBase = base.takeIf { it != 0.0 } ?: 1.0
                        rates.getOrElse(currency.code) { 0.0 } * nonNullBase
                    }
                }
                .debounce(100, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeIt {
                    amount = String.format(Locale.getDefault(), "%.3f", it)
                }

    }

    fun clear() {
        disposable.dispose()
        onCleared()
    }

    private fun onFocusChanged(focused: Boolean) {
        isBase = focused
        notifyChanged()
    }

    private fun notifyChanged() {
        if (!isBase) return
        core.setBase(currency.code, amount.toDoubleOrNull() ?: 0.0)
    }

    class Factory @Inject constructor(
            private val stableIdProvider: StableIdProvider,
            private val core: ConverterViewModel.Core,
            private val modernRxHandler: ModernRxSubscriber.Handler
    ) {

        fun create(currency: Currency): CurrencyItemViewModel {

            return CurrencyItemViewModel(
                    stableIdProvider,
                    currency,
                    core,
                    modernRxHandler
            )

        }

    }

    @Singleton
    class StableIdProvider @Inject constructor() {

        private val ids = mutableMapOf<String, Long>()

        private var lastId = 0L

        operator fun get(name: String): Long = ids.getOrPut(name) { lastId++ }

    }

}