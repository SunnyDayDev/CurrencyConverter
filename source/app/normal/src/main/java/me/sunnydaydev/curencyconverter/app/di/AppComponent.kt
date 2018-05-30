package me.sunnydaydev.curencyconverter.app.di

import dagger.Component
import me.sunnydaydev.curencyconverter.app.App
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponentRequirements
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreComponent
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreProvider
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesDomainComponent
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesDomainProvider

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Component(
        dependencies = [
            CoreProvider::class,
            CurrenciesDomainProvider::class
        ]
)
interface AppComponent:
        ConverterComponentRequirements {

    object Initializer {

        fun init(app: App): AppComponent {

            val core = CoreComponent.Initializer.init(app)

            val currencies = CurrenciesDomainComponent.Initializer.init()

            return DaggerAppComponent.builder()
                    .coreProvider(core)
                    .currenciesDomainProvider(currencies)
                    .build()

        }

    }

}