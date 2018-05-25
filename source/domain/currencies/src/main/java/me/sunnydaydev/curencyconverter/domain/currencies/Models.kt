package me.sunnydaydev.curencyconverter.domain.currencies

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

data class Currency(val alpha3code: String) {

    companion object {

        val EUR get() = Currency("EUR")

    }

}

data class CurrencyRate(val currency: Currency, val rate: Double)