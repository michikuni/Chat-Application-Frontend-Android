package com.company.myapplication.core.config

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    companion object {
        const val KEY_USE_FIRESTORE = "use_firestore"
        private const val TAG = "RemoteConfigManager"
        private const val DEFAULT_FETCH_INTERVAL_SECONDS = 0
    }

    init {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(DEFAULT_FETCH_INTERVAL_SECONDS)
            .build()
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(mapOf(KEY_USE_FIRESTORE to false))
    }

    suspend fun fetchAndActivate(): Boolean = try {
        remoteConfig.fetchAndActivate().await()
    } catch (e: Exception) {
        Log.e(TAG, "fetchAndActivate failed: ${e.message}", e)
        false
    }

    fun useFirestore(): Boolean = remoteConfig.getBoolean(KEY_USE_FIRESTORE)
}
