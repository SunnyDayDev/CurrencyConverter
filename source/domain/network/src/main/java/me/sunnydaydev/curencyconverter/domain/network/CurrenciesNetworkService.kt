package me.sunnydaydev.curencyconverter.domain.network

import io.reactivex.Single
import me.sunnydaydev.curencyconverter.domain.network.models.*
import javax.inject.Inject

/**
 * Created by sunny on 03.06.2018.
 * mail: mail@sunnydaydev.me
 */

interface CurrenciesNetworkService {

    fun getRatesForBase(base: String): Single<CurrencyRatesDto>

    fun getCurrencies(): Single<List<CurrencyDto>>

}

internal class CurrenciesNetworkServiceImpl @Inject constructor(
        private val api: CurrenciesApi
): CurrenciesNetworkService {

    override fun getRatesForBase(base: String): Single<CurrencyRatesDto> {
        return api.getRatesForBase(base).checkResponse()
    }

    override fun getCurrencies(): Single<List<CurrencyDto>> {
        return api.getCurrencies().checkResponse()
    }

}
