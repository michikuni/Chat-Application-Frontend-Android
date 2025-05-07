package com.company.myapplication.data.api

import com.company.myapplication.data.model.request.CreateGroupRequest
import com.company.myapplication.data.model.request.LoginRequest
import com.company.myapplication.data.model.response.LoginResponse
import com.company.myapplication.data.model.request.RegisterRequest
import com.company.myapplication.data.model.response.CreateGroupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest) : Response<LoginResponse>
    @POST("api/groups/create_group")
    suspend fun createGroup(
        @Body request: CreateGroupRequest,
        @Header("Authorization") token: String
    ): Response<CreateGroupResponse>
}