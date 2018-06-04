package me.sunnydaydev.curencyconverter.coregeneral

import android.content.Context
import android.util.Log
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

        val useCrashlytics = initCrashlytics(debug)

        initTimberLog(debug, useCrashlytics)

        initRxPlugins()

    }

    private fun initCrashlytics(debug: Boolean): Boolean {

        if (context.getString(R.string.fabric_api_key) == "api_key_stub") return false

        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(debug)
                .build()
        val crashlytics = Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()
        Fabric.with(context, crashlytics)

        return true

    }

    private fun initTimberLog(debug: Boolean, crashlytics: Boolean) {

        if (debug) {

            Timber.plant(Timber.DebugTree())

        } else if (crashlytics) {

            Fabric.getLogger().logLevel = if (BuildConfig.DEBUG) Log.DEBUG else Log.ERROR
            Timber.plant(CrashlyticsTimberTree())

        }

    }

    private fun initRxPlugins() {

        RxJavaPlugins.setErrorHandler {
            Timber.e((it as? UndeliverableException)?.cause ?: it)
        }

    }

}