package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.response.UserResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getUserInfo(userId: Long): UserResponse? = try {
        val snapshot = firestore.collection(FirestoreCollections.USERS)
            .whereEqualTo("id", userId)
            .limit(1)
            .get()
            .await()
        snapshot.documents.firstOrNull()?.let { doc ->
            UserResponse(
                id = doc.getLong("id") ?: userId,
                name = doc.getString("name") ?: "",
                username = doc.getString("username") ?: "",
                email = doc.getString("email") ?: "",
                avatar = doc.getString("avatar")
            )
        }
    } catch (e: Exception) {
        Log.e("UserFirestore", "getUserInfo: ${e.message}", e)
        null
    }
}
