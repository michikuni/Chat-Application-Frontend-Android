package com.company.myapplication.data.remote.datasource

import android.content.Context
import android.net.Uri
import com.company.myapplication.data.api.FeedApi
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRemoteDataSource @Inject constructor(
    private val feedApi: FeedApi
) {
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
                val body = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it)
                MultipartBody.Part.createFormData("mediaFile", "upload_${System.currentTimeMillis()}.jpg", body)
            }
        }
        return feedApi.postNewsFeed(userId, contentPart, mediaPart)
    }

    suspend fun getAllFeedByUserId(userId: Long): List<FeedDTO> = feedApi.getAllFeed(userId)
}
