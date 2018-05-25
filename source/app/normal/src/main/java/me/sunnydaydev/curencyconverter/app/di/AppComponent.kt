package me.sunnydaydev.curencyconverter.app.di

import dagger.Component
import me.sunnydaydev.curencyconverter.app.App
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponentRequirements
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreComponent
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesComponent
import javax.inject.Scope

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Scope annotation class AppScope

@AppScope
@Component(
        dependencies = [CoreComponent::class, CurrenciesComponent::class]
)
interface AppComponent:
        CoreComponent,
        // Feature requirements providers
        ConverterComponentRequirements {

    object Initializer {

        fun init(app: App): AppComponent {

            val core = CoreComponent.Initializer.init(app)

            val currencies = CurrenciesComponent.Initializer.init()

            return DaggerAppComponent.builder()
                    .coreComponent(core)
                    .currenciesComponent(currencies)
                    .build()

        }

    }

}