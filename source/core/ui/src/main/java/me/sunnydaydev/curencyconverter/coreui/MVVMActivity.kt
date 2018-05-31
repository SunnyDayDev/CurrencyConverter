package me.sunnydaydev.curencyconverter.coreui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel

/**
 * Created by sunny on 03.05.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class MVVMActivity<Binding: ViewDataBinding>: AppCompatActivity() {

    protected abstract val viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModelVariableId: Int

    protected abstract val binding: Binding

    protected abstract fun inject(provider: RequirementsComponentProvider)

    protected abstract fun <T> getViewModel(provider: ViewModelProvider): T

    private lateinit var vm: BaseVewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        proceedInjection()

        vm = getViewModel(ViewModelProviders.of(this, viewModelFactory))

        binding.setVariable(viewModelVariableId, vm)
        lifecycle.addObserver(vm)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun proceedInjection() {

        val requirementsProvider = parent as? RequirementsComponentProvider
                ?: applicationContext as RequirementsComponentProvider

        inject(requirementsProvider)

    }

}