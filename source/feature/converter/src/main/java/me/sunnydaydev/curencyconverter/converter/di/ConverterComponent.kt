package me.sunnydaydev.curencyconverter.converter.di

import dagger.Component
import dagger.Module
import me.sunnydaydev.curencyconverter.converter.ConverterActivity
import me.sunnydaydev.curencyconverter.converter.ConverterViewModel
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreComponent
import me.sunnydaydev.curencyconverter.coreui.di.Injector
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesComponent
import javax.inject.Inject

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface ConverterComponentRequirements:
        CoreComponent,
        CurrenciesComponent

@Component(
        modules = [ConverterBindModule::class],
        dependencies = [ConverterComponentRequirements::class]
)
interface ConverterComponent: Injector<ConverterActivity> {

    object Initializer {

        fun init(requirements: ConverterComponentRequirements): ConverterComponent {
            return DaggerConverterComponent.builder()
                    .converterComponentRequirements(requirements)
                    .build()
        }

    }

}

@Module
internal interface ConverterBindModule

internal class Injection @Inject constructor(
        val vm: ConverterViewModel
)