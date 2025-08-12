package com.company.myapplication.repository

import android.content.Context
import android.util.Log
import com.company.myapplication.data.api.FcmApi
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FcmRepository(context: Context) {
    private fun createRetrofit(context: Context): Retrofit{
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val retrofit = createRetrofit(context)
    private val fcmApi = retrofit.create(FcmApi::class.java)

    suspend fun sendToken(fcmTokenResponse: FcmTokenResponse): Boolean{
        val response = fcmApi.sendTokenFcm(fcmTokenResponse)
        return if (response.isSuccessful){
            true
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
            Log.e("FCM", "Send token failed: HTTP ${response.code()} - $errorMsg")
            false
        }
    }
}