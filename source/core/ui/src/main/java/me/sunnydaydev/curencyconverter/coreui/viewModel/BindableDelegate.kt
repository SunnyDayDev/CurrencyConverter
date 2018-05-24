package me.sunnydaydev.curencyconverter.coreui.viewModel

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by sunny on 28.04.2018.
 * mail: mail@sunnydaydev.me
 */

class BindableDelegate<in R: BaseVewModel, T: Any?> (
        private var value: T,
        private var id: Int?,
        private val br: KClass<*>?,
        private val onChange: ((T) -> Unit)? = null
): ReadWriteProperty<R, T> {

    private var checkedId: Int? = null

    override operator fun getValue(thisRef: R, property: KProperty<*>): T = this.value

    override operator fun setValue(thisRef: R, property: KProperty<*>, value: T) {

        this.value = value

        id = id ?: checkAndGetId(property)

        thisRef.notifyPropertyChanged(id!!)

        onChange?.invoke(value)

    }

    private fun checkAndGetId(property: KProperty<*>): Int {

        return checkedId ?: id ?:
        br?.java?.fields?.associate { it.name to it.getInt(null) }
                ?.let { it[property.name] }
                ?.also { checkedId = it }
        ?: throw IllegalStateException("BR id or BR class not setted.")

    }

}

fun <R: BaseVewModel, T: Any?> bindable(
        initialValue: T,
        id: Int? = null,
        br: KClass<*>? = null,
        onChange: ((T) -> Unit)? = null
): ReadWriteProperty<R, T> = BindableDelegate(initialValue, id, br, onChange)