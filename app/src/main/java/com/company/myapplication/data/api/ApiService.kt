package com.company.myapplication.data.api

import com.company.myapplication.data.model.auth.LoginRequest
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.auth.RegisterRequest
import com.company.myapplication.data.model.user.UserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Void>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/users/all_users/{userId}")
    suspend fun getAllFriendsById(
        @Path("userId") userId: Long
    ): Response<List<UserResponse>>

    @POST("api/friends/add/{userId}")
    suspend fun sendAddRequest(
        @Path("userId") userId: Long,
        @Body request: RequestBody
    ): Response <Void>
}