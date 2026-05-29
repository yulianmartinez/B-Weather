package com.ymd.bweather.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.bold.core.network.di.networkModule
import com.bold.feature.splash.di.splashModule
import com.bold.feature.home.di.homeModule
import com.bold.feature.settings.di.settingsModule

object KoinInitializer {
    fun init(context: Context) {
        startKoin {
            androidLogger()
            androidContext(context)
            modules(
                networkModule,
                homeModule,
                settingsModule,
                splashModule
            )
        }
    }
}
