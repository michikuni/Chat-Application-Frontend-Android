package com.company.myapplication.repository

import android.content.Context
import com.company.myapplication.data.api.ConversationApi
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConversationRepository (context: Context){
    private fun createRetrofit(context: Context): Retrofit{
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
            .build()
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val retrofit = createRetrofit(context)
    private val conversationApi = retrofit.create(ConversationApi::class.java)

    suspend fun createConversation(userId: Long, body: CreateConversation): Boolean{
        val response = conversationApi.createConversation(userId = userId, request = body)
        return response.isSuccessful
    }

    suspend fun getAllMessage(userId: Long, friendId: Long): List<Message>?{
        val response = conversationApi.getAllMessage(userId = userId, friendId = friendId)
        return if (response.isSuccessful){
            response.body()
        } else {
            throw Exception("Lỗi fetch tin nhắn ${response.code()}")
        }
    }

    suspend fun getAllConversation(userId: Long): List<GetConversation>?{
        val response = conversationApi.getAllConversation(userId = userId)
        return if (response.isSuccessful){
            response.body()
        } else {
            throw Exception("Lỗi fetch conversation ${response.code()}")
        }
    }
}