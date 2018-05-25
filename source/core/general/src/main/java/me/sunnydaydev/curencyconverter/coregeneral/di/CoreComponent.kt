package me.sunnydaydev.curencyconverter.coregeneral.di

import android.content.Context
import dagger.*
import me.sunnydaydev.curencyconverter.coregeneral.RxSubscriber
import me.sunnydaydev.modernrx.DisposableBag
import me.sunnydaydev.modernrx.ModernRxSubscriber
import javax.inject.Singleton

@Singleton
@Component(
        modules = [CoreProvidesModule::class]
)
interface CoreComponent {

    val applicationContext: Context

    val modernRxHandler: ModernRxSubscriber.Handler

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

@Module
internal class CoreProvidesModule {

    @Provides
    @Singleton
    fun provideModernRxHandler(subscriber: RxSubscriber): ModernRxSubscriber.Handler {
        return ModernRxSubscriber.Handler(subscriber, DisposableBag())
    }

}