package me.sunnydaydev.curencyconverter.converter

import io.reactivex.Completable
import io.reactivex.Single
import me.sunnydaydev.curencyconverter.coregeneral.Prefs
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepository
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.curencyconverter.domain.currencies.CurrencyRates
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 *
 * Business logic which interact with other modules or system.
 */

@Singleton
internal class ConverterInteractor @Inject constructor(
        private val currenciesRepository: CurrenciesRepository,
        private val prefs: Prefs
) {

    fun getKnownOrder(): Single<List<String>> = Single.fromCallable { prefs.knownCurrenciesOrder }

    fun storeKnownOrder(order: List<String>): Completable = Completable.fromAction {
        prefs.knownCurrenciesOrder = order
    }

    fun getCurrencyRates(code: String): Single<CurrencyRates> =
            currenciesRepository.getRates(code)

    fun getCurrencies(): Single<List<Currency>> = currenciesRepository.getCurrencies()

}