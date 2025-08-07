package com.company.myapplication.repository

import android.app.Activity
import com.company.myapplication.util.UserSharedPreferences
import okhttp3.*

class AuthInterceptor(private val context: Activity) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = UserSharedPreferences.getToken(context)

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            UserSharedPreferences.clearSession(context)
        }

        return response
    }
}
