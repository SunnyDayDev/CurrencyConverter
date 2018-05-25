package me.sunnydaydev.curencyconverter.coregeneral

import timber.log.Timber

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

fun <T> tryOptional(logError: Boolean = false, action: () -> T): T? {
    return try {
        action()
    } catch (e: Throwable) {
        if (logError) Timber.e(e)
        null
    }
}