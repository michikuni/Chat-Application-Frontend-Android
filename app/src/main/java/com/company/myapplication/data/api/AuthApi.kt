package com.company.myapplication.data.api

import com.company.myapplication.data.model.auth.LoginRequest
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.auth.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Void>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}