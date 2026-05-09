package com.company.myapplication.domain.repository

import android.content.Context
import android.net.Uri
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import retrofit2.Response

interface FeedRepository {
    suspend fun postNewsFeed(
        context: Context,
        userId: Long,
        content: String?,
        mediaUri: Uri?
    ): Response<ApiResponse>

    suspend fun getAllFeedByUserId(userId: Long): List<FeedDTO>
}
