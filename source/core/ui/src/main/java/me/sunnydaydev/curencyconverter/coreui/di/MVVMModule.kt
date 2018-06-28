package me.sunnydaydev.curencyconverter.coreui.di

import dagger.Module
import dagger.Provides
import me.sunnydaydev.mvvmkit.util.ViewLifeCycle

/**
 * Created by sunny on 28.06.2018.
 * mail: mail@sunnydaydev.me
 */

@Module
class MVVMModule {

    @get:Provides
    val viewLifeCycle = ViewLifeCycle()

}