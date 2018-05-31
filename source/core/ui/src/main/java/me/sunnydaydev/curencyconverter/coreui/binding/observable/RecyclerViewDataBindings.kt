package me.sunnydaydev.curencyconverter.coreui.binding.observable

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

/**
 * Created by sunny on 31.05.2018.
 * mail: mail@sunnydaydev.me
 */

object RecyclerViewDataBindings {

    @JvmStatic
    @BindingAdapter("scrollToPosition")
    fun bindScrollToPosition(view: RecyclerView, position: Command<Int>) {
        position.handle(view::scrollToPosition)
    }

}