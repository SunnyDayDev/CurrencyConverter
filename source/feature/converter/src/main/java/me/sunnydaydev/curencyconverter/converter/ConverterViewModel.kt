package me.sunnydaydev.curencyconverter.converter

import android.databinding.Bindable
import me.sunnydaydev.curencyconverter.coreui.viewModel.BaseVewModel
import me.sunnydaydev.modernrx.ModernRxSubscriber
import javax.inject.Inject

/**
 * Created by sunny on 24.05.2018.
 * mail: mail@sunnydaydev.me
 */
internal class ConverterViewModel @Inject constructor(
        modernRxHandler: ModernRxSubscriber.Handler
): BaseVewModel(modernRxHandler, BR::class) {

    @Bindable val test = "Success"

}