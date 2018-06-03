package me.sunnydaydev.curencyconverter.domain.network.models

import io.reactivex.Single
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

/**
 * Created by sunny on 03.06.2018.
 * mail: mail@sunnydaydev.me
 */

internal fun <R: Response<D>, D> Single<R>.checkResponse(): Single<D> = flatMap {
    when {
        !it.status.success -> {
            val errors = it.status.errors?.map(::ApiRequestError) ?: emptyList()
            Single.error(ApiRequestFailedError(errors))
        }
        it.data == null -> Single.error(DataIsNullError())
        else -> Single.just(it.data!!)
    }
}

internal interface Response<T> {

    val data: T?
    val status: Status

    @Serializable
    data class Status(
            val success: Boolean,
            @Optional val errors: List<Error>? = null
    )

    @Serializable
    data class Error(
            val code: Int,
            val description: String
    )

}

@Serializable
internal data class CurrenciesResponse(
        override val data: List<CurrencyDto>?,
        override val status: Response.Status
): Response<List<CurrencyDto>>

@Serializable
internal data class RatesResponse(
        override val data: CurrencyRatesDto?,
        override val status: Response.Status
): Response<CurrencyRatesDto>