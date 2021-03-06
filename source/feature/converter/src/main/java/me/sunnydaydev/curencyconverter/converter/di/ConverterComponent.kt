package me.sunnydaydev.curencyconverter.converter.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import me.sunnydaydev.curencyconverter.converter.ConverterFragment
import me.sunnydaydev.curencyconverter.converter.ConverterViewModel
import me.sunnydaydev.curencyconverter.coregeneral.di.ComponentRequirements
import me.sunnydaydev.curencyconverter.coregeneral.di.CoreProvider
import me.sunnydaydev.curencyconverter.coreui.di.Injector
import me.sunnydaydev.curencyconverter.domain.currencies.di.CurrenciesDomainProvider
import me.sunnydaydev.mvvmkit.viewModel.InjectableViewModelFactory
import me.sunnydaydev.mvvmkit.viewModel.ViewModelKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface ConverterComponentRequirements: ComponentRequirements,
        CoreProvider,
        CurrenciesDomainProvider

@Singleton
@Component(
        modules = [ConverterBindModule::class],
        dependencies = [ConverterComponentRequirements::class]
)
interface ConverterComponent: Injector<ConverterFragment> {

    object Initializer {

        fun init(requirements: ConverterComponentRequirements): ConverterComponent {
            return DaggerConverterComponent.builder()
                    .converterComponentRequirements(requirements)
                    .build()
        }

    }

}

@Module
internal interface ConverterBindModule {

    @Binds
    @IntoMap
    @ViewModelKey(ConverterViewModel::class)
    fun bindViewModel(vm: ConverterViewModel): ViewModel

}

internal class Injection @Inject constructor(
        val vmFactory: InjectableViewModelFactory
)