package me.sunnydaydev.curencyconverter.coreui

import androidx.databinding.ViewDataBinding
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.mvvmkit.MVVMFragment
import me.sunnydaydev.mvvmkit.util.ViewLifeCycle
import javax.inject.Inject

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class InjectableMVVMFragment<Binding: ViewDataBinding>: MVVMFragment<Binding>()  {

    @set:Inject
    override lateinit var viewLifeCycle: ViewLifeCycle

    override fun proceedInjection() {

        val context = context ?: return

        val requirementsProvider = parentFragment as? RequirementsComponentProvider
                ?: activity as? RequirementsComponentProvider
                ?: context.applicationContext as RequirementsComponentProvider

        inject(requirementsProvider)

    }

    protected abstract fun inject(provider: RequirementsComponentProvider)

}