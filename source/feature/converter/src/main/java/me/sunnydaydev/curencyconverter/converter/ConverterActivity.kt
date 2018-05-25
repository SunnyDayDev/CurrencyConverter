package me.sunnydaydev.curencyconverter.converter

import me.sunnydaydev.curencyconverter.converter.databinding.ActivityMainBinding
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponent
import me.sunnydaydev.curencyconverter.converter.di.ConverterComponentRequirements
import me.sunnydaydev.curencyconverter.converter.di.Injection
import me.sunnydaydev.curencyconverter.coregeneral.di.RequirementsComponentProvider
import me.sunnydaydev.curencyconverter.coreui.MVVMActivity
import me.sunnydaydev.curencyconverter.coreui.setContentBinding
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import javax.inject.Inject

class ConverterActivity : MVVMActivity<ActivityMainBinding>() {

    @Inject
    @Suppress("MemberVisibilityCanBePrivate")
    internal lateinit var injection: Injection

    override val vm: BaseVewModel by lazy { injection.vm }

    override val viewModelVariableId = BR.vm

    override val binding by lazy<ActivityMainBinding> {
        setContentBinding(R.layout.activity_main)
    }

    override fun inject(provider: RequirementsComponentProvider) {
        val requirement: ConverterComponentRequirements = provider.getRequirementsComponent()
        ConverterComponent.Initializer.init(requirement).inject(this)
    }

}