package me.sunnydaydev.curencyconverter.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import me.sunnydaydev.curencyconverter.converter.databinding.ConverterFragmentBinding
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponent
import me.sunnydaydev.curencyconverter.converter.di.Injection
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.curencyconverter.coreui.MVVMFragment
import me.sunnydaydev.curencyconverter.coreui.util.inflateBinding
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import javax.inject.Inject

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

class ConverterFragment: MVVMFragment<ConverterFragmentBinding>() {

    @Inject
    internal lateinit var injection: Injection

    override val viewModelVariableId = BR.vm

    override val vm: BaseVewModel by lazy { injection.vm }

    override fun inject(provider: RequirementsComponentProvider) {
        ConverterComponent.Initializer.init(provider.getComponentRequirements())
                .inject(this)
    }

    override fun onCreateBinding(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): ConverterFragmentBinding = inflater.inflateBinding(R.layout.converter_fragment, container)


}