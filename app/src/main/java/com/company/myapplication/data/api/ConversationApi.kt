package com.company.myapplication.data.api

import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ConversationApi {
    @POST("api/chats/createConversation/{userId}")
    suspend fun createConversation(
        @Path("userId") userId: Long,
        @Body request: CreateConversation
    ): Response<Void>

    @GET("api/chats/allMessage/{userId}")
    suspend fun getAllMessage(
        @Path("userId") userId: Long,
        @Query("friendId") friendId: Long
    ): Response<List<Message>>

    @GET("api/chats/allConversation/{userId}")
    suspend fun getAllConversation(
        @Path("userId") userId: Long
    ): Response<List<GetConversation>>

    @Multipart
    @POST("api/chats/sendMediaFile/{userId}")
    suspend fun sendMediaFile(
        @Path("userId") userId: Long,
        @Query("friendId") friendId: Long,  // gửi friendId như form field
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
}