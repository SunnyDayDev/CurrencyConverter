package me.sunnydaydev.curencyconverter.app

import android.app.Application
import me.sunnydaydev.curencyconverter.app.di.AppComponent
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreComponent
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */
class App: Application(), RequirementsComponentProvider {

    private val appComponent: AppComponent by lazy { AppComponent.Initializer.init(this) }

    override fun <T : CoreComponent> getRequirementsComponent(): T = appComponent as T

}