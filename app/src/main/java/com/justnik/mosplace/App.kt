package com.justnik.mosplace

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(ApiKeys.mapKit)
    }
}