package me.sunnydaydev.curencyconverter.coregeneral.di

import android.content.Context
import dagger.*
import me.sunnydaydev.curencyconverter.coregeneral.AppInitializer
import me.sunnydaydev.curencyconverter.coregeneral.AppInitializerIml
import javax.inject.Singleton

@Singleton
@Component(
        modules = [CoreProvidesModule::class]
)
interface CoreComponent: CoreProvider, AppInitializerProvider {

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

interface AppInitializerProvider {

    val appInitializer: AppInitializer

}

@Module
internal interface CoreProvidesModule {

    @Binds
    fun bindAppInitializer(impl: AppInitializerIml): AppInitializer

}