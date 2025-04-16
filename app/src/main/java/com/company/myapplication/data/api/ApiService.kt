package com.company.myapplication.data.api

import com.company.myapplication.data.model.LoginRequest
import com.company.myapplication.data.model.LoginResponse
import com.company.myapplication.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest) : Response<LoginResponse>
}