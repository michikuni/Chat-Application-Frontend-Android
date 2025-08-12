package com.company.myapplication.data.api

import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendApi {
    @GET("api/users/all_users/{userId}")
    suspend fun getAllFriendsById(
        @Path("userId") userId: Long
    ): Response<List<UserResponse>>

    @POST("api/friends/add/{userId}")
    suspend fun sendAddRequest(
        @Path("userId") userId: Long,
        @Body request: RequestBody
    ): Response<Void>

    @GET("api/friends/pending/{userId}")
    suspend fun getPendingFriends(
        @Path("userId") userId: Long
    ): Response<List<FriendResponse>>

    @GET("api/friends/request/{userId}")
    suspend fun getRequestFriends(
        @Path("userId") userId: Long
    ): Response<List<FriendResponse>>

    @POST("api/friends/accept/{friendshipId}")
    suspend fun acceptedFriendRequest(
        @Path("friendshipId") friendshipId: Long
    ): Response<Void>

    @POST("api/friends/cancel/{friendshipId}")
    suspend fun cancelFriendRequest(
        @Path("friendshipId") friendshipId: Long
    ): Response<Void>
}