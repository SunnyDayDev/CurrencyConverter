package me.sunnydaydev.curencyconverter.app

import android.app.Application
import me.sunnydaydev.curencyconverter.app.di.AppComponent
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreProvider
import me.sunnydaydev.curencyconverter.coregeneral.di.InjectionProvider

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */
class App: Application(), InjectionProvider {

    private val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this) }

    override fun <T : CoreProvider> getInjectionProvider(): T = appComponent as T

}