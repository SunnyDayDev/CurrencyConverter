package com.medicine.ima.coreui

import android.databinding.ViewDataBinding
import android.os.Bundle
import com.medicine.ima.coreui.viewModel.BaseVewModel
import javax.inject.Provider

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

interface MVVMInjection<VM> {
    val viewModel: Provider<VM>
}

abstract class MVVMActivity<
        Binding: ViewDataBinding,
        VM: BaseVewModel,
        Injection: MVVMInjection<VM>
>: InjectableActivity<Injection>() {

    protected val vm by injectable { viewModel }

    protected abstract val viewModelVariableId: Int

    protected abstract val binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(viewModelVariableId, vm)
        lifecycle.addObserver(vm)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}