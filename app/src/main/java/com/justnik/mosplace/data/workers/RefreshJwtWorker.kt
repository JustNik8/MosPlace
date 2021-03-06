package com.justnik.mosplace.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.justnik.mosplace.data.network.apiservices.AuthService
import com.justnik.mosplace.data.network.authmodels.RefreshToken
import com.justnik.mosplace.data.prefs.UserPrefs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class RefreshJwtWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val authService: AuthService,
    private val userPrefs: UserPrefs
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val refreshToken = userPrefs.jwtRefreshToken ?: return Result.failure()
        return try {
            val response = authService.refreshJwtAccessToken(RefreshToken(refreshToken))
            if (response.isSuccessful) {
                userPrefs.jwtAccessToken = response.body()?.get("access")?.asString
                Result.success()
            }
            Result.failure()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }


    companion object {
        const val NAME = "RefreshJwtWorker"
        fun makeRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RefreshJwtWorker>(1, TimeUnit.DAYS)
                .build()
        }
    }
}