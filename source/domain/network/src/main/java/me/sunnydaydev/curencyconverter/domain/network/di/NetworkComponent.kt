package me.sunnydaydev.curencyconverter.domain.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.stringBased
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.JSON
import me.sunnydaydev.curencyconverter.domain.network.CurrenciesApi
import me.sunnydaydev.curencyconverter.domain.network.CurrenciesNetworkService
import me.sunnydaydev.curencyconverter.domain.network.CurrenciesNetworkServiceImpl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by sunny on 03.06.2018.
 * mail: mail@sunnydaydev.me
 */

interface NetworkServicesProvider {

    val currenciesNetworkService: CurrenciesNetworkService

}

@Component(modules = [NetworkModule::class, NetworkServicesModule::class])
interface NetworkComponent: NetworkServicesProvider {

    object Initializer {

        fun init(): NetworkComponent {
            return DaggerNetworkComponent.builder()
                    .build()
        }

    }

}


@Module
internal class NetworkModule {

    @Provides
    fun providesOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    fun provideJSON(): JSON {
        return JSON()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, json: JSON): Retrofit.Builder {

        val mediaType = MediaType.parse("application/json")!!
        val jsonConverter = stringBased(mediaType, json::parse, json::stringify)

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(jsonConverter)
                .client(client)

    }

    @Provides
    fun provideCurrencyApi(retrofit: Retrofit.Builder): CurrenciesApi {

        return retrofit
                .baseUrl(CurrenciesApi.Urls.HOST)
                .build()
                .create(CurrenciesApi::class.java)

    }

}

@Module
internal interface NetworkServicesModule {

    @Binds
    fun bindCurrenciesNetworkService(impl: CurrenciesNetworkServiceImpl): CurrenciesNetworkService

}