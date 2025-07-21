package com.company.myapplication.repository

import android.app.Activity
import android.content.Context
import okhttp3.*

class AuthInterceptor(private val context: Activity) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("jwt_token", null)

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
