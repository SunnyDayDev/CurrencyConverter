package me.sunnydaydev.curencyconverter.coregeneral

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * Created by sunny on 04.06.2018.
 * mail: mail@sunnydaydev.me
 */

class CrashlyticsTimberTree: Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = true

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        Crashlytics.log(priority, tag, message)

        if (priority == Log.ERROR || priority == Log.ASSERT) {

            Crashlytics.setInt("Exception.Priority", priority)
            Crashlytics.setString("Exception.Tag", tag)
            Crashlytics.setString("Exception.Message", message)

            Crashlytics.logException(t ?: Exception(message))

        }

    }

}