package me.sunnydaydev.curencyconverter.domain.network

import io.reactivex.Single
import me.sunnydaydev.curencyconverter.domain.network.models.CurrenciesResponse
import me.sunnydaydev.curencyconverter.domain.network.models.RatesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal interface CurrenciesApi {

    object Urls {
        const val HOST = "https://currencyconverter.sunnydaydev.me/v2/"
    }

    @GET("rates")
    fun getRatesForBase(@Query("base") base: String): Single<RatesResponse>

    @GET("currencies")
    fun getCurrencies(): Single<CurrenciesResponse>

}