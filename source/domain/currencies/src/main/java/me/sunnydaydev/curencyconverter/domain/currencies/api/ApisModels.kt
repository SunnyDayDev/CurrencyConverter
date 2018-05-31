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

@Serializable
data class CountryInfo(
    val name: String,
    val topLevelDomain: List<String>,
    val alpha2Code: String,
    val alpha3Code: String,
    val callingCodes: List<String>,
    val capital: String,
    val altSpellings: List<String>,
    val region: String,
    val subregion: String,
    val population: Int,
    val latlng: List<Double>,
    val demonym: String,
    val area: Double?,
    val gini: Double?,
    val timezones: List<String>,
    val borders: List<String>,
    val nativeName: String,
    val numericCode: String?,
    val currencies: List<Currency>,
    val languages: List<Language>,
    val translations: Map<String, String?>,
    val flag: String,
    val regionalBlocs: List<RegionalBloc>,
    val cioc: String?
) {

    @Serializable
    data class RegionalBloc(
        val acronym: String,
        val name: String,
        val otherAcronyms: List<String>,
        val otherNames: List<String?>
    )

    @Serializable
    data class Currency(
        val code: String?,
        val name: String?,
        val symbol: String?
    )

    @Serializable
    data class Language(
        val iso639_1: String?,
        val iso639_2: String?,
        val name: String?,
        val nativeName: String?
    )

}