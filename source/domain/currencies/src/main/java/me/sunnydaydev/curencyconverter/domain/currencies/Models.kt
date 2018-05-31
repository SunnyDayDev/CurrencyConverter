package me.sunnydaydev.curencyconverter.domain.currencies

import android.net.Uri

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

data class Currency(val code: String, val name: String, val flagUrl: Uri)

data class CurrencyRates(val base: String, val rates: Map<String, Double>)