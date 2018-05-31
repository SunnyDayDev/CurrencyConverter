package me.sunnydaydev.curencyconverter.converter

import android.databinding.Bindable
import com.github.nitrico.lastadapter.StableId
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import me.sunnydaydev.curencyconverter.coregeneral.tryOptional
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.curencyconverter.coreui.viewModel.bindable
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.modernrx.OptionalDisposable
import me.sunnydaydev.modernrx.disposeBy
import timber.log.Timber
import java.text.DecimalFormat
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
        private val core: ConverterViewModel.Core
): BaseVewModel(), StableId {

    companion object {

        private val FORMAT = DecimalFormat().apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 4
        }

    }

    override val stableId: Long get() = stableIdProvider[currency.code]

    @get:Bindable var amount by bindable("") {
        if (focused) notifyChanged()
    }

    @get:Bindable val code by bindable(currency.code)
    @get:Bindable val description by bindable(currency.name)
    @get:Bindable val flag by bindable(currency.flagUrl)
    @get:Bindable var focused by bindable(false)

    private val amountSource: Observable<Double> get() {

        return Observables.combineLatest(core.baseAmount, core.rates) { base, rates ->
            val nonNullBase = base.takeIf { it != 0.0 } ?: 1.0
            rates.getOrElse(currency.code) { 0.0 } * nonNullBase
        }

    }

    init {

        val sourceDisposable = OptionalDisposable()
        core.base
                .flatMap {
                    val isBase = currency.code == it
                    val source = amountSource.map { isBase to it }
                    sourceDisposable.dispose()
                    if (!isBase)  source.disposeBy(sourceDisposable)
                    else source.firstElement().toObservable()
                }
                .subscribeIt { (isBase, amount) ->

                    focused = isBase
                    if (!isBase) {
                        this.amount = FORMAT.format(amount)
                    }

                }

    }

    internal fun clear() {
        onCleared()
    }

    fun onClicked() {
        focused = false
        notifyChanged()
    }

    private fun notifyChanged() {
        core.setBase(currency.code, tryOptional { FORMAT.parse(amount).toDouble() } ?: 0.0)
    }

    class Factory @Inject constructor(
            private val stableIdProvider: StableIdProvider,
            private val core: ConverterViewModel.Core
    ) {

        fun create(currency: Currency): CurrencyItemViewModel =
                CurrencyItemViewModel(stableIdProvider, currency, core)

    }

    @Singleton
    class StableIdProvider @Inject constructor() {

        private val ids = mutableMapOf<String, Long>()

        private var lastId = 0L

        operator fun get(name: String): Long = ids.getOrPut(name) { lastId++ }

    }

}