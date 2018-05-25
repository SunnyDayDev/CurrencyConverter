package me.sunnydaydev.curencyconverter.domain.currencies

import io.reactivex.Single
import me.sunnydaydev.curencyconverter.domain.currencies.api.CurrenciesApi
import javax.inject.Inject

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface CurrenciesRepository {

    fun getRatesFor(base: Currency): Single<List<CurrencyRate>>

}

internal class CurrenciesRepositoryImpl @Inject constructor(
        private val api: CurrenciesApi,
        private val mapper: CurrenciesMapper
): CurrenciesRepository {

    override fun getRatesFor(base: Currency): Single<List<CurrencyRate>> {
        
        return api.getRatesForBase(base.alpha3code)
                .map { mapper.mapRates(it.rates) }

    }

}