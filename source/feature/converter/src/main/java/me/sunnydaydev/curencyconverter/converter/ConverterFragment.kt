package me.sunnydaydev.curencyconverter.converter

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import me.sunnydaydev.curencyconverter.converter.databinding.ConverterFragmentBinding
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponent
import me.sunnydaydev.curencyconverter.converter.di.Injection
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.curencyconverter.coreui.InjectableMVVMFragment
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.mvvmkit.util.inflateBinding
import me.sunnydaydev.mvvmkit.viewModel.get
import javax.inject.Inject

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

class ConverterFragment: InjectableMVVMFragment<ConverterFragmentBinding>() {

    @Inject
    internal lateinit var injection: Injection

    override val viewModelVariableId = BR.vm

    override val viewModelFactory: ViewModelProvider.Factory by lazy { injection.vmFactory }

    override fun getViewModel(provider: ViewModelProvider): BaseVewModel =
            provider[ConverterViewModel::class]

    override fun inject(provider: RequirementsComponentProvider) {
        ConverterComponent.Initializer.init(provider.getComponentRequirements())
                .inject(this)
    }

    override fun onCreateBinding(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflateBinding<ConverterFragmentBinding>(R.layout.converter_fragment, container)
            .also { it.bindings = ConverterBindings() }

}