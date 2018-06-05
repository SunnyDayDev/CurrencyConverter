package me.sunnydaydev.curencyconverter.converter

import android.databinding.Bindable
import com.github.nitrico.lastadapter.StableId
import me.sunnydaydev.curencyconverter.coregeneral.tryOptional
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.mvvmkit.observable.bindable
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal class CurrencyItemViewModel(
        private val currency: Currency,
        private val onItemClickListener: (CurrencyItemViewModel) -> Unit,
        private val stableIdProvider: StableIdProvider,
        private val core: ConverterViewModel.Core
): BaseVewModel(), StableId {

    companion object {

        private val FORMAT = DecimalFormat().apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 4
            decimalFormatSymbols.apply {
                groupingSeparator = ','
                decimalSeparator = '.'
            }
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

    init {

        core.getAmount(currency.code)
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

    fun onInputClicked() {
        focused = false
        notifyChanged()
    }

    fun onItemClicked() {
        onItemClickListener(this)
    }

    private fun notifyChanged() {
        core.setBase(currency.code, tryOptional { FORMAT.parse(amount).toDouble() } ?: 0.0)
    }

    class Factory @Inject constructor(
            private val stableIdProvider: StableIdProvider,
            private val core: ConverterViewModel.Core
    ) {

        fun create(
                currency: Currency,
                onItemClickListener: (CurrencyItemViewModel) -> Unit
        ): CurrencyItemViewModel = CurrencyItemViewModel(
                currency, onItemClickListener, stableIdProvider, core)

    }

    @Singleton
    class StableIdProvider @Inject constructor() {

        private val ids = mutableMapOf<String, Long>()

        private var lastId = 0L

        operator fun get(name: String): Long = ids.getOrPut(name) { lastId++ }

    }

}