package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.ApiResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private companion object { const val TAG = "FeedFirestore" }

    suspend fun postNewsFeedTextOnly(userId: Long, content: String?): ApiResponse {
        return try {
            val id = System.currentTimeMillis()
            firestore.collection(FirestoreCollections.FEEDS)
                .document(id.toString())
                .set(
                    mapOf(
                        "id" to id,
                        "posterId" to userId,
                        "content" to content,
                        "createdAt" to id,
                        "mediaFile" to null
                    )
                ).await()
            ApiResponse(message = "Đăng bài thành công")
        } catch (e: Exception) {
            Log.e(TAG, "postNewsFeed: ${e.message}", e)
            ApiResponse(error = e.message ?: "Đăng bài thất bại")
        }
    }

    suspend fun getAllFeedByUserId(userId: Long): List<FeedDTO> = try {
        firestore.collection(FirestoreCollections.FEEDS)
            .get().await()
            .documents.map { doc ->
                FeedDTO(
                    id = doc.getLong("id") ?: 0L,
                    posterId = doc.getLong("posterId") ?: 0L,
                    content = doc.getString("content"),
                    createdAt = (doc.getLong("createdAt") ?: 0L).toString(),
                    mediaFile = doc.getString("mediaFile")
                )
            }.sortedByDescending { it.createdAt }
    } catch (e: Exception) {
        Log.e(TAG, "getAllFeedByUserId: ${e.message}", e); emptyList()
    }
}
