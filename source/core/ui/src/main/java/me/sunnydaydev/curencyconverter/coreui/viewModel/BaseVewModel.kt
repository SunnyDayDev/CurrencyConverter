package me.sunnydaydev.curencyconverter.coreui.viewModel

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import android.support.annotation.CallSuper
import me.sunnydaydev.modernrx.*

/**
 * Created by sunny on 28.04.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class BaseVewModel: ViewModel(), LifecycleObserver, Observable, ModernRx {

    private val disposerBag = DisposableBag()
    final override val modernRxDisposer: ModernRx.Disposer = ModernRx.Disposer(disposerBag)

    // region Observable

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.notifyCallbacks(this, fieldId, null)
    }

    // endregion Observable

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposerBag.enabled = false
    }

}