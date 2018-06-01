package me.sunnydaydev.curencyconverter.domain.currencies

import android.net.Uri
import io.reactivex.Observable
import io.reactivex.Single
import me.sunnydaydev.curencyconverter.domain.currencies.api.CountryInfo
import me.sunnydaydev.curencyconverter.domain.currencies.api.CurrenciesApi
import me.sunnydaydev.curencyconverter.domain.currencies.api.RestCountriesApi
import javax.inject.Inject

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface CurrenciesRepository {

    fun getCurrencies(codes: Set<String>? = null): Single<List<Currency>>

    fun getRates(base: String): Single<CurrencyRates>

}

internal class CurrenciesRepositoryImpl @Inject constructor(
        private val api: CurrenciesApi,
        private val countriesApi: RestCountriesApi
): CurrenciesRepository {

    companion object {
        private val skipCountries = setOf("BV", "AQ")
    }

    private val currenciesCache = mutableMapOf<String, Currency>()

    override fun getRates(base: String): Single<CurrencyRates> {
        
        return api.getRatesForBase(base)
                .map {
                    CurrencyRates(it.base, it.rates)
                }

    }

    override fun getCurrencies(codes: Set<String>?): Single<List<Currency>> {
        return api.getRatesForBase("EUR")
                .map {
                    val allCodes = it.rates.keys + "EUR"
                    if (codes == null) allCodes
                    else allCodes.filter { codes.contains(it) }
                }
                .flatMap {
                    Observable.concat(it.map { getCurrencyByCode(it).toObservable() })
                            .toList()
                }
    }

    private fun getCurrencyByCode(code: String): Single<Currency> = Single.defer {
        val cached = currenciesCache[code]
        if (cached != null) Single.just(cached)
        else countriesApi.getCountryInfoByCurrencyCode(code)
                .map {
                    it.first {
                        !skipCountries.contains(it.alpha2Code) &&
                                it.currencies.find { it.code == code } != null

                    }
                }
                .map { country ->
                    val currencyInfo = country.currencies.first { it.code == code }
                    Currency(
                            code = code,
                            name = currencyInfo.name ?: "",
                            flagUrl = flagUri(country)
                    )
                }
                .doOnSuccess { currenciesCache[code] = it }
    }

    private fun flagUri(countryInfo: CountryInfo): Uri {
        val code = countryInfo.alpha2Code.toLowerCase()
        return Uri.parse("http://www.countryflags.io/$code/flat/64.png")
    }

}