package me.sunnydaydev.curencyconverter.coregeneral.rx

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

fun <T> Observable<T>.retryWithDelay(delay: Long, timeUnit: TimeUnit): Observable<T> {
    return retryWhen { errors ->
        errors.flatMap { Observable.timer(delay, timeUnit) }
    }
}