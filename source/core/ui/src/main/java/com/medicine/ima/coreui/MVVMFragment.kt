package com.medicine.ima.coreui

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medicine.ima.coreui.viewModel.BaseVewModel
import javax.inject.Provider

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class MVVMFragment<
        Binding: ViewDataBinding,
        VM: BaseVewModel,
        Injection: MVVMInjection<VM>
>: InjectableFragment<Injection>() {

    protected val vm by injectable { viewModel }

    protected abstract val viewModelVariableId: Int

    @Suppress("MemberVisibilityCanBePrivate")
    protected var binding: Binding? = null

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

    protected abstract fun onCreateBinding(inflater: LayoutInflater,
                                           container: ViewGroup?,
                                           savedInstanceState: Bundle?): Binding

}