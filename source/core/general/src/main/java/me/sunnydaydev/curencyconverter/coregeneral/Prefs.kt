package me.sunnydaydev.curencyconverter.coregeneral

import android.content.Context
import javax.inject.Inject

/**
 * Created by sunny on 04.06.2018.
 * mail: mail@sunnydaydev.me
 */

interface Prefs {

    var knownCurrenciesOrder: List<String>

}

internal class PrefsImpl @Inject constructor(
        context: Context
): Prefs {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    override var knownCurrenciesOrder: List<String>
        get() {
            return tryOptional(true) {
                prefs.getString("knownCurrenciesOrder", null)?.split(", ")
            } ?: emptyList()
        }
        set(value) {
            prefs.edit()
                    .putString("knownCurrenciesOrder", value.joinToString(", "))
                    .apply()
        }

}