package com.medicine.ima.coreui.binding

import android.databinding.BaseObservable

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

class Focus<T: Any>: BaseObservable() {

    var target: T? = null
        set(value) {
            field = value
            notifyChange()
        }

    fun forTarget(value: T): Boolean {
        return value == target
    }

}