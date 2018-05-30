package me.sunnydaydev.curencyconverter.coreui

import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class MVVMFragment<Binding: ViewDataBinding>: Fragment()  {

    // region Abstract

    protected abstract val viewModelVariableId: Int
    protected abstract val vm: BaseVewModel

    protected abstract fun inject(provider: RequirementsComponentProvider)

    protected abstract fun onCreateBinding(inflater: LayoutInflater,
                                           container: ViewGroup?,
                                           savedInstanceState: Bundle?): Binding

    // endregion

    @Suppress("MemberVisibilityCanBePrivate")
    protected var binding: Binding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        proceedInjection(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(vm)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return onCreateBinding(inflater, container, savedInstanceState)
                .apply { setVariable(viewModelVariableId, vm) }
                .root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
        binding = null
    }

    private fun proceedInjection(context: Context) {

        val requirementsProvider = parentFragment as? RequirementsComponentProvider
                ?: activity as? RequirementsComponentProvider
                ?: context.applicationContext as RequirementsComponentProvider

        inject(requirementsProvider)

    }

}