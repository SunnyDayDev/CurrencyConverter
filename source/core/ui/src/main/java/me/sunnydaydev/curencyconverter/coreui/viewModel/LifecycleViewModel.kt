package me.sunnydaydev.curencyconverter.coreui.viewModel

import androidx.lifecycle.LifecycleObserver
import me.sunnydaydev.mvvmkit.util.ViewLifeCycle

/**
 * Created by sunny on 28.06.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class LifecycleViewModel(
        private val viewLifeCycle: ViewLifeCycle
): BaseVewModel(), LifecycleObserver {

    init {
        viewLifeCycle.addObserver(this)
    }

    override fun onCleared() {
        super.onCleared()
        viewLifeCycle.removeObserver(this)
    }

}