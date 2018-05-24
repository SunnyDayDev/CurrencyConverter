package me.sunnydaydev.curencyconverter.coreui.di

/**
 * Created by sunny on 28.04.2018.
 * mail: mail@sunnydaydev.me
 */
interface Injector<T> {
    fun inject(target: T)
}