package me.sunnydaydev.curencyconverter.domain.currencies.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal interface CurrenciesApi {

    object Urls {
        const val HOST = "https://currencyconverter.sunnydaydev.me"
    }

    @GET("latest")
    fun getRatesForBase(@Query("base") base: String): Single<CurrencyRateResponse>

}