package com.ymd.bweather

import android.app.Application
import com.ymd.bweather.di.KoinInitializer

class BWeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer.init(this)
    }
}
