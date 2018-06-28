package me.sunnydaydev.curencyconverter.converter

import android.os.Bundle
import me.sunnydaydev.mvvmkit.BaseMVVMActivity

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

class ConverterActivity : BaseMVVMActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.converter_activity)
    }

}