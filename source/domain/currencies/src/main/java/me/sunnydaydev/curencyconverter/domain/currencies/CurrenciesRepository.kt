package me.sunnydaydev.curencyconverter.domain.currencies

import android.net.Uri
import io.reactivex.Single
import me.sunnydaydev.curencyconverter.domain.network.CurrenciesNetworkService
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
        private val currenciesNetworkService: CurrenciesNetworkService
): CurrenciesRepository {

    override fun getRates(base: String): Single<CurrencyRates> {
        
        return currenciesNetworkService.getRatesForBase(base)
                .map { CurrencyRates(it.base, it.rates) }

    }

    override fun getCurrencies(codes: Set<String>?): Single<List<Currency>> {
        return currenciesNetworkService.getCurrencies()
                .map {
                    it.map { (name, code, flagUrl) ->
                        Currency(name, code, Uri.parse(flagUrl))
                    }
                }
    }

}