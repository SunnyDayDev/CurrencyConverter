package me.sunnydaydev.curencyconverter.converter

import io.reactivex.Single
import me.sunnydaydev.curencyconverter.domain.currencies.CurrenciesRepository
import me.sunnydaydev.curencyconverter.domain.currencies.Currency
import me.sunnydaydev.curencyconverter.domain.currencies.CurrencyRate
import javax.inject.Inject

/**
 * Created by sunny on 25.05.2018.
 * mail: mail@sunnydaydev.me
 *
 * Business logic which interact with other modules or system.
 */

internal class ConverterInteractor @Inject constructor(
        private val currenciesRepository: CurrenciesRepository
) {

    fun getCurrencyRates(base: Currency): Single<List<CurrencyRate>> =
            currenciesRepository.getRatesFor(base)

}