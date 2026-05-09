package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun sendToken(fcmTokenResponse: FcmTokenResponse): Boolean = try {
        firestore.collection(FirestoreCollections.FCM_TOKENS)
            .document(fcmTokenResponse.userId.toString())
            .set(
                mapOf(
                    "userId" to fcmTokenResponse.userId,
                    "token" to fcmTokenResponse.token,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
        true
    } catch (e: Exception) {
        Log.e("FcmFirestore", "sendToken: ${e.message}", e); false
    }
}
