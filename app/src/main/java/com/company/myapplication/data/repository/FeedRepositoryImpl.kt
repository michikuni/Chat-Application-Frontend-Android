package com.company.myapplication.data.repository

import android.content.Context
import android.net.Uri
import com.company.myapplication.core.config.BackendSelector
import com.company.myapplication.core.config.BackendType
import com.company.myapplication.core.util.ToastHelper
import com.company.myapplication.data.firestore.datasource.FeedFirestoreDataSource
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import com.company.myapplication.data.remote.datasource.FeedRemoteDataSource
import com.company.myapplication.domain.repository.FeedRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepositoryImpl @Inject constructor(
    private val backendSelector: BackendSelector,
    private val remote: FeedRemoteDataSource,
    private val firestore: FeedFirestoreDataSource
) : FeedRepository {

    override suspend fun postNewsFeed(
        context: Context,
        userId: Long,
        content: String?,
        mediaUri: Uri?
    ): Response<ApiResponse> {
        return when (backendSelector.current()) {
            BackendType.REMOTE -> remote.postNewsFeed(context, userId, content, mediaUri)
            BackendType.FIRESTORE -> {
                if (mediaUri != null) {
                    ToastHelper.showFeatureUnavailable(context)
                    return Response.error(
                        501,
                        "{\"error\":\"Chức năng hiện tại không khả dụng\"}"
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                }
                val api = firestore.postNewsFeedTextOnly(userId, content)
                Response.success(api)
            }
        }
    }

    override suspend fun getAllFeedByUserId(userId: Long): List<FeedDTO> =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getAllFeedByUserId(userId)
            BackendType.FIRESTORE -> firestore.getAllFeedByUserId(userId)
        }
}
