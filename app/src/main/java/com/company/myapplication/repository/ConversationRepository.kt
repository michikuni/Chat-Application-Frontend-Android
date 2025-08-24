package com.company.myapplication.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.company.myapplication.data.api.ConversationApi
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
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
            .baseUrl(ApiConfig.BASE_URL)
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

    suspend fun sendMediaFile(context: Context, uri: Uri, userId: Long, friendId: Long) {
        val file = uriToFile(context, uri)

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val response = conversationApi.sendMediaFile(userId = userId, friendId = friendId, file = body)

        if (response.isSuccessful) {
            Log.e("✅ Upload thành công:", "${response.body()?.string()}")
        } else {
            Log.e("❌ Upload thất bại: ", "${response.errorBody()?.string()}")
        }
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        val outputStream = file.outputStream()
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}