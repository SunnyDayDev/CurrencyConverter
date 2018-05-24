package me.sunnydaydev.curencyconverter.coregeneral

import me.sunnydaydev.modernrx.Subscriber
import me.sunnydaydev.modernrx.UnhandledErrorException
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal class RxSubscriber @Inject constructor(): Subscriber() {

    override fun onUnhandledError(e: UnhandledErrorException) {
        Timber.e(e)
    }

}