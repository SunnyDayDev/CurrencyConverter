package me.sunnydaydev.curencyconverter.coreui

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.sunnydaydev.curencyconverter.coregeneral.di.InjectionProvider
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class MVVMActivity<Binding: ViewDataBinding>: AppCompatActivity() {

    protected abstract val vm: BaseVewModel

    protected abstract val viewModelVariableId: Int

    protected abstract val binding: Binding

    protected abstract fun inject(provider: InjectionProvider)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(applicationContext as InjectionProvider)
        binding.setVariable(viewModelVariableId, vm)
        lifecycle.addObserver(vm)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}