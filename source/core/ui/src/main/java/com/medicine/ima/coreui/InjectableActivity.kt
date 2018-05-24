package com.medicine.ima.coreui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.medicine.ima.core.App
import com.medicine.ima.core.di.AppProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by sunny on 28.04.2018.
 * mail: mail@sunnydaydev.me
 */

abstract class InjectableActivity<Injection: Any>: AppCompatActivity() {

    private lateinit var injection: Injection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = applicationContext as App
        inject(app.getAppComponent())

    }

    protected abstract fun inject(appContext: Context)

    @Inject
    protected fun setInjection(injection: Injection) {
        this.injection = injection
    }

    protected fun <T> injectable(provider: Injection.() -> Provider<T>): Lazy<T> {
        return lazy { provider(injection).get() }
    }

}