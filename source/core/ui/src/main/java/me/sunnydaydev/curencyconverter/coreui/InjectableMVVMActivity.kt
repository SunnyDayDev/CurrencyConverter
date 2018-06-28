package me.sunnydaydev.curencyconverter.coreui

import androidx.databinding.ViewDataBinding
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.mvvmkit.MVVMActivity
import me.sunnydaydev.mvvmkit.util.ViewLifeCycle
import javax.inject.Inject

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class InjectableMVVMActivity<Binding: ViewDataBinding>: MVVMActivity<Binding>() {

    @set:Inject
    override lateinit var viewLifeCycle: ViewLifeCycle

    override fun proceedInjection() {

        val requirementsProvider = parent as? RequirementsComponentProvider
                ?: applicationContext as RequirementsComponentProvider

        inject(requirementsProvider)

    }

    protected abstract fun inject(provider: RequirementsComponentProvider)

}