package me.sunnydaydev.curencyconverter.converter.di

import dagger.Component
import dagger.Module
import me.sunnydaydev.curencyconverter.converter.ConverterActivity
import me.sunnydaydev.curencyconverter.converter.ConverterViewModel
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreProvider
import me.sunnydaydev.curencyconverter.coreui.di.Injector
import javax.inject.Inject

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface ConverterComponentRequirementsProvider:
    CoreProvider

@Component(
        modules = [ConverterBindModule::class],
        dependencies = [ConverterComponentRequirementsProvider::class]
)
interface ConverterComponent: Injector<ConverterActivity> {

    object Initializer {

        fun init(provider: ConverterComponentRequirementsProvider): ConverterComponent {
            return DaggerConverterComponent.builder()
                    .converterComponentRequirementsProvider(provider)
                    .build()
        }

    }

}

@Module
internal interface ConverterBindModule

internal class Injection @Inject constructor(
        val vm: ConverterViewModel
)