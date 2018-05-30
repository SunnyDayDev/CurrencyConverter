package me.sunnydaydev.curencyconverter.coreui.viewModel

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.sunnydaydev.curencyconverter.coregeneral.tryOptional
import me.sunnydaydev.modernrx.CompleteHandler
import me.sunnydaydev.modernrx.ErrorHandler
import me.sunnydaydev.modernrx.ModernRxSubscriber
import me.sunnydaydev.modernrx.ResultHandler
import kotlin.properties.ReadWriteProperty

/**
 * Created by sunny on 28.04.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class BaseVewModel: ViewModel(), LifecycleObserver, Observable {

    abstract val modernRxHandler: ModernRxSubscriber.Handler

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

    // region ModernRx

    protected fun Completable.subscribeIt(
            onError: ErrorHandler? = null,
            onComplete: CompleteHandler? = null
    ) : Disposable = modernRxHandler.subscribeIt(this, onError, onComplete)

    protected fun <T> Maybe<T>.subscribeIt(
            onError: ErrorHandler? = null,
            onComplete: CompleteHandler? = null,
            onSuccess: ResultHandler<T>? = null
    ) : Disposable = modernRxHandler.subscribeIt(this, onError, onComplete, onSuccess)

    protected fun <T> Single<T>.subscribeIt(
            onError: ErrorHandler? = null,
            onSuccess: ResultHandler<T>? = null
    ) : Disposable = modernRxHandler.subscribeIt(this, onError, onSuccess)

    protected fun <T> io.reactivex.Observable<T>.subscribeIt(
            onError: ErrorHandler? = null,
            onComplete: CompleteHandler? = null,
            onNext: ResultHandler<T>? = null
    ) : Disposable = modernRxHandler.subscribeIt(this, onError, onComplete, onNext)

    protected fun <T> Flowable<T>.subscribeIt(
            onError: ErrorHandler? = null,
            onComplete: CompleteHandler? = null,
            onNext: ResultHandler<T>? = null
    ) : Disposable = modernRxHandler.subscribeIt(this, onError, onComplete, onNext)

    // endregion

}