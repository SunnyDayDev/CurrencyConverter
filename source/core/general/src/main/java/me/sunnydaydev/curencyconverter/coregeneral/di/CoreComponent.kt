package me.sunnydaydev.curencyconverter.coregeneral.di

import android.content.Context
import dagger.*
import javax.inject.Singleton

@Singleton
@Component(
        modules = [CoreProvidesModule::class]
)
interface CoreComponent: CoreProvider {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): CoreComponent.Builder

        fun build(): CoreComponent

    }

    object Initializer {

        fun init(context: Context): CoreComponent {
            return DaggerCoreComponent.builder()
                    .context(context)
                    .build()
        }

    }

}

interface CoreProvider {

    val applicationContext: Context

}

@Module
internal class CoreProvidesModule {

}