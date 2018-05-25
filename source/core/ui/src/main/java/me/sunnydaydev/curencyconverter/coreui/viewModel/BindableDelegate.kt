package me.sunnydaydev.curencyconverter.coreui.viewModel

import me.sunnydaydev.curencyconverter.coregeneral.tryOptional
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by sunny on 28.04.2018.
 * mail: mail@sunnydaydev.me
 */

class BindableDelegate<in R: BaseVewModel, T: Any?> (
        private var value: T,
        private var id: Int?,
        private val onChange: ((T) -> Unit)? = null
): ReadWriteProperty<R, T> {

    companion object {

        private val dataBindingFields by lazy<Map<String, Int>> {

            val clazz = tryOptional(logError = true) {
                Class.forName("com.android.databinding.library.baseAdapters.BR")
            } ?: return@lazy emptyMap()

            clazz.fields.associate { it.name to it.getInt(null) }

        }

    }

    private var checkedId: Int? = null

    override operator fun getValue(thisRef: R, property: KProperty<*>): T = this.value

    override operator fun setValue(thisRef: R, property: KProperty<*>, value: T) {

        this.value = value

        id = id ?: checkAndGetId(property)

        thisRef.notifyPropertyChanged(id!!)

        onChange?.invoke(value)

    }

    private fun checkAndGetId(property: KProperty<*>): Int {

        return checkedId ?: id
                ?: dataBindingFields[property.name]?.also { checkedId = it }
                ?: throw IllegalStateException("Unknown bindable property.")

    }

}

fun <R: BaseVewModel, T: Any?> bindable(
        initialValue: T,
        id: Int? = null,
        onChange: ((T) -> Unit)? = null
): ReadWriteProperty<R, T> = BindableDelegate(initialValue, id, onChange)