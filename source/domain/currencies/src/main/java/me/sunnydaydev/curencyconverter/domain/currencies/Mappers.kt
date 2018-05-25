package me.sunnydaydev.curencyconverter.domain.currencies

import javax.inject.Inject

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal interface CurrenciesMapper {

    fun mapRates(raw: Map<String, Double>): List<CurrencyRate>

    fun mapCurrency(alpha3Code: String): Currency

}

internal class CurrenciesMapperImpl @Inject constructor() : CurrenciesMapper {

    override fun mapRates(raw: Map<String, Double>): List<CurrencyRate> = raw.entries.map {
        val currency = mapCurrency(it.key)
        CurrencyRate(currency = currency, rate = it.value)
    }

    override fun mapCurrency(alpha3Code: String): Currency = Currency(alpha3code = alpha3Code)

}