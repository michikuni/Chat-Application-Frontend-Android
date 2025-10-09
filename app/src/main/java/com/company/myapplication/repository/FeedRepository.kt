package com.company.myapplication.repository

import android.content.Context
import android.net.Uri
import com.company.myapplication.data.api.FeedApi
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.concurrent.TimeUnit

class FeedRepository (context: Context){
    private fun createRetrofit(context: Context): Retrofit {
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

    private val feedApi = retrofit.create(FeedApi::class.java)

    suspend fun postNewsFeed(
        context: Context,
        userId: Long,
        content: String?,
        mediaUri: Uri?
    ): Response<ApiResponse> {
        val contentPart = content?.let {
            RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        val mediaPart = mediaUri?.let { uri ->
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            bytes?.let {
                val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it)
                MultipartBody.Part.createFormData("mediaFile", "upload_${System.currentTimeMillis()}.jpg", requestBody)
            }
        }

        return feedApi.postNewsFeed(userId, contentPart, mediaPart)
    }

    suspend fun getAllFeedByUserId(userId: Long): List<FeedDTO> {
        return feedApi.getAllFeed(userId)
    }
}