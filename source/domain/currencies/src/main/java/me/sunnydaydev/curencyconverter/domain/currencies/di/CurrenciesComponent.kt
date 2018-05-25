package me.sunnydaydev.curencyconverter.domain.currencies.di

import dagger.Binds
import dagger.Component
import dagger.Module
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesMapper
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesMapperImpl
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepository
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepositoryImpl
import me.sunnydaydev.curencyconverter.domain.currencies.api.di.ApiModule

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Component(
        modules = [CurrenciesModule::class]
)
interface CurrenciesComponent {

    val currenciesRepository: CurrenciesRepository

    object Initializer {

        fun init(): CurrenciesComponent {

           return DaggerCurrenciesComponent.builder()
                    .build()

        }

    }

}

@Module(includes = [ApiModule::class])
internal interface CurrenciesModule {

    @Binds
    fun bindCurrenciesMapper(impl: CurrenciesMapperImpl): CurrenciesMapper

    @Binds
    fun bindCurrenciesRepository(impl: CurrenciesRepositoryImpl): CurrenciesRepository

}