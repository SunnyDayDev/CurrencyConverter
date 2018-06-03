package me.sunnydaydev.curencyconverter.domain.currencies.di

import dagger.Binds
import dagger.Component
import dagger.Module
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepository
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepositoryImpl
import me.sunnydaydev.curencyconverter.domain.network.di.NetworkServicesProvider
import javax.inject.Singleton

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Singleton
@Component(
        modules = [CurrenciesModule::class],
        dependencies = [NetworkServicesProvider::class]
)
interface CurrenciesDomainComponent: CurrenciesDomainProvider {

    object Initializer {

        fun init(network: NetworkServicesProvider): CurrenciesDomainComponent {

           return DaggerCurrenciesDomainComponent.builder()
                   .networkServicesProvider(network)
                   .build()

        }

    }

}

interface CurrenciesDomainProvider {

    val currenciesRepository: CurrenciesRepository

}

@Module
internal interface CurrenciesModule {

    @Binds
    @Singleton
    fun bindCurrenciesRepository(impl: CurrenciesRepositoryImpl): CurrenciesRepository

}