package com.company.myapplication.data.remote.datasource

import android.util.Log
import com.company.myapplication.data.api.FcmApi
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmRemoteDataSource @Inject constructor(
    private val fcmApi: FcmApi
) {
    suspend fun sendToken(fcmTokenResponse: FcmTokenResponse): Boolean {
        return try {
            val response = fcmApi.sendTokenFcm(fcmTokenResponse)
            if (response.isSuccessful) {
                true
            } else {
                Log.e("FcmRemote", "Send token failed: HTTP ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("FcmRemote", "Send token error: ${e.message}", e)
            false
        }
    }
}
