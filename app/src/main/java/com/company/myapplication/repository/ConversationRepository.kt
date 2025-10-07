package com.company.myapplication.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.company.myapplication.data.api.ConversationApi
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.CreateConversationGroup
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class ConversationRepository(context: Context) {

    private fun createRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
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

    suspend fun createMessage(userId: Long, body: CreateConversation): Boolean {
        return try {
            val response = conversationApi.createMessage(userId = userId, request = body)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi tạo tin nhắn: ${e.message}", e)
            false
        }
    }

    suspend fun findConversation(userId: Long, friendId: Long): Long {
        return try {
            val response = conversationApi.findConversation(userId, friendId)
            response.body() ?: -1
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi tìm conversation: ${e.message}", e)
            -1
        }
    }

    suspend fun createConversationGroup(data: CreateConversationGroup, uri: Uri, context: Context) {
        try {
            val gson = Gson()
            val json = gson.toJson(data)
            val dataRequestBody = json.toRequestBody("application/json".toMediaType())

            val file = uriToFile(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaType())
            val multipartFile = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = conversationApi.createConversationGroup(dataRequestBody, multipartFile)

            if (response.isSuccessful) {
                Log.i("ConversationRepo", "✅ Tạo nhóm thành công")
            } else {
                Log.e("ConversationRepo", "❌ Tạo nhóm thất bại: ${response.code()} | ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi khi tạo nhóm: ${e.message}", e)
        }
    }

    suspend fun getAllMessage(conversationId: Long): List<Message>? {
        return try {
            val response = conversationApi.getAllMessage(conversationId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ConversationRepo", "Lỗi fetch tin nhắn ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi kết nối getAllMessage: ${e.message}", e)
            null
        }
    }

    suspend fun getAllConversation(userId: Long): List<ConversationDTO>? {
        return try {
            val response = conversationApi.getAllConversation(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ConversationRepo", "Lỗi fetch conversation ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi kết nối getAllConversation: ${e.message}", e)
            null
        }
    }

    suspend fun sendMediaFile(context: Context, uri: Uri, userId: Long, conversationId: Long) {
        try {
            val file = uriToFile(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = conversationApi.sendMediaFile(userId, conversationId, body)

            if (response.isSuccessful) {
                val result = response.body()?.string() ?: "Không có nội dung trả về"
                Log.i("ConversationRepo", "✅ Upload thành công: $result")
            } else {
                Log.e("ConversationRepo", "❌ Upload thất bại: ${response.code()} | ${response.errorBody()?.string()}")
            }
        } catch (e: IOException) {
            Log.e("ConversationRepo", "Lỗi IO khi upload file: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi không xác định khi upload file: ${e.message}", e)
        }
    }

    suspend fun updateTheme(context: Context, conversationId: Long, color: List<String>) {
        try {
            val response = conversationApi.updateTheme(conversationId, color)
            if (response.isSuccessful) {
                Toast.makeText(context, "Cập nhật chủ đề thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Cập nhật chủ đề thất bại", Toast.LENGTH_SHORT).show()
                Log.e("ConversationRepo", "Lỗi updateTheme: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("ConversationRepo", "Lỗi khi cập nhật theme: ${e.message}", e)
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
