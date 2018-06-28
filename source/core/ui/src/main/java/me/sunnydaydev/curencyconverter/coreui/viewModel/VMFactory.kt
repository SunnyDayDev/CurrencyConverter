package me.sunnydaydev.curencyconverter.coreui.viewModel

import androidx.lifecycle.ViewModel
import dagger.MapKey
import me.sunnydaydev.mvvmkit.viewModel.MVVMViewModelFactory
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * Created by sunny on 09.06.2018.
 * mail: mail@sunnydaydev.me
 */

@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

private typealias VMClass = Class<out ViewModel>
private typealias VMCreators = Map<VMClass, () -> ViewModel>
typealias DaggerVMCreators = Map<VMClass, @JvmSuppressWildcards Provider<ViewModel>>

class VMFactory @Inject constructor(creators: DaggerVMCreators) : MVVMViewModelFactory() {

    override val creators: VMCreators = creators.mapValues { { it.value.get() } }

}