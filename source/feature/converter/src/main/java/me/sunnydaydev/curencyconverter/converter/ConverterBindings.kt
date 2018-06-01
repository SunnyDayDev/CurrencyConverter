package me.sunnydaydev.curencyconverter.converter

import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import me.sunnydaydev.mvvmkit.binding.RecyclerViewBindingsAdapter
import java.security.MessageDigest

/**
 * Created by sunny on 30.05.2018.
 * mail: mail@sunnydaydev.me
 */

internal class ConverterBindings {

    companion object {

        @JvmStatic
        val flagTransformation: Transformation<Bitmap> get() = FlagTransformation()

    }

    val currencyItemsMap = RecyclerViewBindingsAdapter.BindingMap(R.id.binding_converter_itemsMap)
            .map<CurrencyItemViewModel>(BR.vm, R.layout.converter_currency_item_layout)

}

/**
 * Original flags has bottom and top offsets, so we need to crop it.
 */
private class FlagTransformation: BitmapTransformation() {

    companion object {

        private const val VERSION = 1

        private const val ID = "me.sunnydaydev.ConverterFlagTransformation:$VERSION"

    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray())
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val offset = (toTransform.height * 0.2).toInt()
        return Bitmap.createBitmap(
                toTransform,
                0, offset,
                toTransform.width, toTransform.height - offset * 2
        )

    }


}