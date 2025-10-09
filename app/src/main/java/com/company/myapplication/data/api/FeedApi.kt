package com.company.myapplication.data.api

import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FeedApi {
    @Multipart
    @POST("api/feed/post_newsfeed/{userId}")
    suspend fun postNewsFeed(
        @Path("userId") userId: Long,
        @Part("content") content: RequestBody?,
        @Part mediaFile: MultipartBody.Part?
    ): Response<ApiResponse>

    @GET("api/feed/getAllFeed/{userId}")
    suspend fun getAllFeed(@Path("userId") userId: Long): List<FeedDTO>
}