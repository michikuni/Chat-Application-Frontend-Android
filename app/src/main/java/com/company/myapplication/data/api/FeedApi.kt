package com.company.myapplication.data.api

import com.company.myapplication.data.model.response.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.*

interface FeedApi {
    @Multipart
    @POST("api/feed/post_newsfeed/{userId}")
    suspend fun postNewsFeed(
        @Path("userId") userId: Long,
        @Part("content") content: RequestBody?,
        @Part mediaFile: MultipartBody.Part?
    ): Response<ApiResponse>
}