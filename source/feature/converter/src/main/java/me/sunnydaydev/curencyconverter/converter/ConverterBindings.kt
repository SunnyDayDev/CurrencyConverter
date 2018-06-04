package me.sunnydaydev.curencyconverter.converter

import me.sunnydaydev.mvvmkit.binding.RecyclerViewBindingsAdapter

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal class ConverterBindings {

    val currencyItemsMap = RecyclerViewBindingsAdapter.BindingMap(R.id.binding_converter_itemsMap)
            .map<CurrencyItemViewModel>(BR.vm, R.layout.converter_currency_item_layout)

}