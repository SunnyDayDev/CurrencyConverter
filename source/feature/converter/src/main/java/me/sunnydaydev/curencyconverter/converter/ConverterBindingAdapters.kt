package me.sunnydaydev.curencyconverter.converter

import android.databinding.BindingAdapter
import android.databinding.adapters.ListenerUtil
import android.support.v7.widget.RecyclerView
import com.github.nitrico.lastadapter.LastAdapter

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal object ConverterBindingAdapters {

    @JvmStatic
    @BindingAdapter("currency_items")
    fun bindCurrencies(view: RecyclerView, items: List<CurrencyItemViewModel>) {

        var adapterPair: AdapterPair<CurrencyItemViewModel>? =
                ListenerUtil.getListener(view, R.id.binding_converter_currency_items)

        if (adapterPair?.items === items) return

        val adapter = LastAdapter(items, BR.vm, true)
                .map<CurrencyItemViewModel>(R.layout.converter_currency_item_layout)
                .into(view)

        adapterPair = AdapterPair(adapter, items)

        ListenerUtil.trackListener(view, adapterPair, R.id.binding_converter_currency_items)

    }

    private data class AdapterPair<T>(
            val adapter: LastAdapter,
            val items: List<T>
    )

}