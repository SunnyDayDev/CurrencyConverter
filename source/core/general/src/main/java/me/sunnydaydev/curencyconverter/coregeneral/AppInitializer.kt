package me.sunnydaydev.curencyconverter.coregeneral

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by sunny on 04.06.2018.
 * mail: mail@sunnydaydev.me
 */

interface AppInitializer {
    fun init(debug: Boolean)
}

internal class AppInitializerIml @Inject constructor(
        private val context: Context
): AppInitializer {

    override fun init(debug: Boolean) {

        initCrashlytics(debug)

        initTimberLog(debug)

        initRxPlugins()

    }

    private fun initCrashlytics(debug: Boolean) {

        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(debug)
                .build()
        val crashlytics = Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()
        Fabric.with(context, crashlytics)

    }

    private fun initTimberLog(debug: Boolean) {

        if (debug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant()
        }
    }

    private fun initRxPlugins() {

        RxJavaPlugins.setErrorHandler {
            Timber.e((it as? UndeliverableException)?.cause ?: it)
        }

    }

}