package me.sunnydaydev.curencyconverter.coregeneral.di

import android.content.Context
import me.sunnydaydev.modernrx.ModernRxSubscriber

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface CoreProvider {

    fun provideApplicationContext(): Context

    fun provideModernRxHandler(): ModernRxSubscriber.Handler

}


interface InjectionProvider {

    fun <T: CoreProvider> getInjectionProvider(): T

}