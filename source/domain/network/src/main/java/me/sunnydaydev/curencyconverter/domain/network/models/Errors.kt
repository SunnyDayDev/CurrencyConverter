package me.sunnydaydev.curencyconverter.domain.network.models

/**
 * Created by sunny on 03.06.2018.
 * mail: mail@sunnydaydev.me
 */

open class ApiError: Throwable()

class ApiRequestError(val code: Int, override val message: String): ApiError() {
    internal constructor(error: Response.Error): this(error.code, error.description)
}

class ApiRequestFailedError(val errors: List<ApiRequestError>): ApiError()

class DataIsNullError: ApiError()