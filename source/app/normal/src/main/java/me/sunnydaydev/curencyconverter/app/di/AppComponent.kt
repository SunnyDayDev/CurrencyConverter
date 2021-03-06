package me.sunnydaydev.curencyconverter.app.di

import dagger.Component
import me.sunnydaydev.curencyconverter.app.App
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponentRequirements
import me.sunnydaydev.curencyconverter.coregeneral.di.AppInitializerProvider
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreComponent
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreProvider
import me.sunnydaydev.curencyconverter.coreui.di.Injector
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesDomainComponent
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesDomainProvider
import me.sunnydaydev.curencyconverter.domain.network.di.NetworkComponent

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Component(
        dependencies = [
            CoreProvider::class,
            AppInitializerProvider::class,
            CurrenciesDomainProvider::class
        ]
)
interface AppComponent: Injector<App>,
        ConverterComponentRequirements {

    object Initializer {

        fun init(app: App): AppComponent {

            val core = CoreComponent.Initializer.init(app)

            val network = NetworkComponent.Initializer.init()

            val currencies = CurrenciesDomainComponent.Initializer.init(network)

            return DaggerAppComponent.builder()
                    .coreProvider(core)
                    .appInitializerProvider(core)
                    .currenciesDomainProvider(currencies)
                    .build()

        }

    }

}