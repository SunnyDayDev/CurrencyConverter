package me.sunnydaydev.curencyconverter.domain.network.models

import kotlinx.serialization.Serializable

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Serializable
data class CurrencyDto(val code: String, val name: String, val flagUrl: String)

@Serializable
data class CurrencyRatesDto(
        val base: String,
        val date: String,
        val rates: Map<String, Double>
)
