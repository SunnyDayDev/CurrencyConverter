package me.sunnydaydev.curencyconverter.app

import android.app.Application
import me.sunnydaydev.curencyconverter.app.di.AppComponent
import me.sunnydaydev.curencyconverter.coregeneral.AppInitializer
import me.sunnydaydev.curencyconverter.coregeneral.di.ComponentRequirements
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import javax.inject.Inject

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */
class App: Application(), RequirementsComponentProvider {

    @Inject
    lateinit var initializer: AppInitializer

    private val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this) }

    override fun <T : ComponentRequirements> getComponentRequirements(): T = appComponent as T

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        initializer.init(BuildConfig.DEBUG)
    }

}