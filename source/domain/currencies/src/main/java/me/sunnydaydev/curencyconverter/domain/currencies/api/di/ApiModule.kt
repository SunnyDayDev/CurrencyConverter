package me.sunnydaydev.curencyconverter.domain.currencies.api.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.stringBased
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.JSON
import me.sunnydaydev.curencyconverter.domain.currencies.api.CurrenciesApi
import me.sunnydaydev.curencyconverter.domain.currencies.api.RestCountriesApi
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 */

@Module
internal class ApiModule {

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

    @Provides
    fun provideRestCountriesApi(retrofit: Retrofit.Builder): RestCountriesApi {

        return retrofit
                .baseUrl(RestCountriesApi.Urls.HOST)
                .build()
                .create(RestCountriesApi::class.java)

    }

}