package com.company.myapplication.data.remote.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.company.myapplication.data.api.ConversationApi
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.CreateConversationGroup
import com.company.myapplication.data.model.chat.Message
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRemoteDataSource @Inject constructor(
    private val conversationApi: ConversationApi
) {
    private companion object { const val TAG = "ConversationRemote" }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return file
    }

    suspend fun createMessage(userId: Long, body: CreateConversation): Boolean = try {
        conversationApi.createMessage(userId = userId, request = body).isSuccessful
    } catch (e: Exception) {
        Log.e(TAG, "createMessage: ${e.message}", e); false
    }

    suspend fun findConversation(userId: Long, friendId: Long): Long = try {
        conversationApi.findConversation(userId, friendId).body() ?: -1L
    } catch (e: Exception) {
        Log.e(TAG, "findConversation: ${e.message}", e); -1L
    }

    suspend fun createConversationGroup(data: CreateConversationGroup, uri: Uri, context: Context) {
        try {
            val json = Gson().toJson(data)
            val dataBody = json.toRequestBody("application/json".toMediaType())
            val file = uriToFile(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaType())
            val multipart = MultipartBody.Part.createFormData("file", file.name, requestFile)
            conversationApi.createConversationGroup(dataBody, multipart)
        } catch (e: Exception) {
            Log.e(TAG, "createConversationGroup: ${e.message}", e)
        }
    }

    suspend fun getAllMessage(conversationId: Long): List<Message>? = try {
        val response = conversationApi.getAllMessage(conversationId)
        if (response.isSuccessful) response.body() else null
    } catch (e: Exception) {
        Log.e(TAG, "getAllMessage: ${e.message}", e); null
    }

    suspend fun getAllConversation(userId: Long): List<ConversationDTO>? = try {
        val response = conversationApi.getAllConversation(userId)
        if (response.isSuccessful) response.body() else null
    } catch (e: Exception) {
        Log.e(TAG, "getAllConversation: ${e.message}", e); null
    }

    suspend fun sendMediaFile(context: Context, uri: Uri, userId: Long, conversationId: Long) {
        try {
            val file = uriToFile(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            conversationApi.sendMediaFile(userId, conversationId, body)
        } catch (e: Exception) {
            Log.e(TAG, "sendMediaFile: ${e.message}", e)
        }
    }

    suspend fun updateTheme(context: Context, conversationId: Long, color: List<String>) {
        try {
            val response = conversationApi.updateTheme(conversationId, color)
            val msg = if (response.isSuccessful) "Cập nhật chủ đề thành công" else "Cập nhật chủ đề thất bại"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "updateTheme: ${e.message}", e)
        }
    }
}
