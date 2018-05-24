package me.sunnydaydev.curencyconverter.app.di

import dagger.Component
import me.sunnydaydev.curencyconverter.app.App
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponentRequirementsProvider
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreComponent
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreProvider

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Component(
        dependencies = [CoreProvider::class]
)
interface AppComponent:
        CoreProvider,
        // Feature requirements providers
        ConverterComponentRequirementsProvider {

    object Initializer {

        fun init(app: App): AppComponent {

            val core = CoreComponent.Initializer.init(app)

            return DaggerAppComponent.builder()
                    .coreProvider(core)
                    .build()

        }

    }

}