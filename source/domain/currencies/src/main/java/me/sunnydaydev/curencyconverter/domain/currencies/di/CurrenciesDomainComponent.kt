package me.sunnydaydev.curencyconverter.domain.currencies.di

import dagger.Binds
import dagger.Component
import dagger.Module
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepository
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepositoryImpl
import me.sunnydaydev.curencyconverter.domain.currencies.api.di.ApiModule
import javax.inject.Singleton

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Singleton
@Component(
        modules = [CurrenciesModule::class]
)
interface CurrenciesDomainComponent: CurrenciesDomainProvider {

    object Initializer {

        fun init(): CurrenciesDomainComponent {

           return DaggerCurrenciesDomainComponent.builder()
                    .build()

        }

    }

}

interface CurrenciesDomainProvider {

    val currenciesRepository: CurrenciesRepository

}

@Module(includes = [ApiModule::class])
internal interface CurrenciesModule {

    @Binds
    @Singleton
    fun bindCurrenciesRepository(impl: CurrenciesRepositoryImpl): CurrenciesRepository

}