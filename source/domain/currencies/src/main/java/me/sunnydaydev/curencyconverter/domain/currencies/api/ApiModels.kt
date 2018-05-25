package me.sunnydaydev.curencyconverter.domain.currencies.api

import kotlinx.serialization.Serializable

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Serializable
internal data class CurrencyRateResponse(
        val base: String,
        val date: String,
        val rates: Map<String, Double>
)