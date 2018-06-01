package me.sunnydaydev.curencyconverter.coreui

import android.databinding.ViewDataBinding
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.mvvmkit.MVVMActivity

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class InjectableMVVMActivity<Binding: ViewDataBinding>: MVVMActivity<Binding>() {

    override fun proceedInjection() {

        val requirementsProvider = parent as? RequirementsComponentProvider
                ?: applicationContext as RequirementsComponentProvider

        inject(requirementsProvider)

    }

    protected abstract fun inject(provider: RequirementsComponentProvider)

}