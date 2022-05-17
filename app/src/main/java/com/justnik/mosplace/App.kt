package com.justnik.mosplace

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.justnik.mosplace.data.workers.RefreshJwtWorker
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        setupMapKitFactory()
        enqueuePeriodicWork()
    }

    private fun setupMapKitFactory(){
        try{
            MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun enqueuePeriodicWork(){
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            RefreshJwtWorker.NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            RefreshJwtWorker.makeRequest()
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }
}