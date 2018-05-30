package me.sunnydaydev.curencyconverter.coreui.binding

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImageUrl(imageView: ImageView, url: String?) {
        bindImageUrl(imageView, url?.let { Uri.parse(it) })
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImageUrl(imageView: ImageView, url: Uri?) {

        val glide = Glide.with(imageView)
        glide.clear(imageView)

        url ?: return

        glide.load(url).into(imageView)

    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "focused")
    fun inverseBindingFocused(view: View): Boolean {
        return view.isFocused
    }

    @JvmStatic
    @BindingAdapter(value = ["focused", "focusedAttrChanged"], requireAll = false)
    fun bindFocused(view: View, focused: Boolean?, inverse: InverseBindingListener?) {

        // TODO: ListenerUtil.trackListener(...)
        view.onFocusChangeListener = null

        if (focused != null) {

            if (focused != view.isFocused) {
                if (focused) {
                    view.requestFocus()
                } else {
                    view.clearFocus()
                }
            }

            view.setOnFocusChangeListener { _, _ -> inverse?.onChange() }

        }

    }

}