package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private companion object {
        const val TAG = "FriendFirestore"
        const val STATUS_PENDING = "PENDING"
        const val STATUS_ACCEPTED = "ACCEPTED"
    }

    private fun docToUser(doc: com.google.firebase.firestore.DocumentSnapshot): UserResponse =
        UserResponse(
            id = doc.getLong("id") ?: 0L,
            name = doc.getString("name") ?: "",
            username = doc.getString("username") ?: "",
            email = doc.getString("email") ?: "",
            avatar = doc.getString("avatar")
        )

    suspend fun getAllFriendsById(userId: Long): List<UserResponse> = try {
        val friendships = firestore.collection(FirestoreCollections.FRIENDSHIPS)
            .whereEqualTo("status", STATUS_ACCEPTED)
            .get().await()

        val friendIds = friendships.documents.mapNotNull { doc ->
            val sender = doc.getLong("senderId") ?: return@mapNotNull null
            val receiver = doc.getLong("receiverId") ?: return@mapNotNull null
            when (userId) {
                sender -> receiver
                receiver -> sender
                else -> null
            }
        }
        if (friendIds.isEmpty()) emptyList()
        else firestore.collection(FirestoreCollections.USERS)
            .whereIn("id", friendIds.take(10))
            .get().await()
            .documents.map { docToUser(it) }
    } catch (e: Exception) {
        Log.e(TAG, "getAllFriendsById: ${e.message}", e)
        emptyList()
    }

    suspend fun getFriendByEmail(email: String): UserResponse? = try {
        firestore.collection(FirestoreCollections.USERS)
            .whereEqualTo("email", email)
            .limit(1)
            .get().await()
            .documents.firstOrNull()?.let { docToUser(it) }
    } catch (e: Exception) {
        Log.e(TAG, "getFriendByEmail: ${e.message}", e)
        null
    }

    suspend fun sendAddRequest(userId: Long, receiverEmail: String): String? = try {
        val receiver = getFriendByEmail(receiverEmail) ?: return "Không tìm thấy người dùng"
        val friendshipId = System.currentTimeMillis()
        firestore.collection(FirestoreCollections.FRIENDSHIPS)
            .document(friendshipId.toString())
            .set(
                mapOf(
                    "id" to friendshipId,
                    "senderId" to userId,
                    "receiverId" to receiver.id,
                    "status" to STATUS_PENDING,
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
        "Đã gửi yêu cầu kết bạn"
    } catch (e: Exception) {
        Log.e(TAG, "sendAddRequest: ${e.message}", e)
        "Lỗi không xác định"
    }

    private suspend fun friendshipsBy(field: String, userId: Long): List<FriendResponse> {
        val snapshot = firestore.collection(FirestoreCollections.FRIENDSHIPS)
            .whereEqualTo(field, userId)
            .whereEqualTo("status", STATUS_PENDING)
            .get().await()

        val partnerField = if (field == "senderId") "receiverId" else "senderId"
        val partnerIds = snapshot.documents.mapNotNull { it.getLong(partnerField) }
        if (partnerIds.isEmpty()) return emptyList()

        val users = firestore.collection(FirestoreCollections.USERS)
            .whereIn("id", partnerIds.take(10))
            .get().await()
            .documents.associateBy { it.getLong("id") }

        return snapshot.documents.mapNotNull { doc ->
            val partnerId = doc.getLong(partnerField) ?: return@mapNotNull null
            val u = users[partnerId] ?: return@mapNotNull null
            FriendResponse(
                friendshipId = doc.getLong("id") ?: 0L,
                id = u.getLong("id") ?: 0L,
                name = u.getString("name") ?: "",
                username = u.getString("username") ?: "",
                email = u.getString("email") ?: "",
                avatar = u.getString("avatar")
            )
        }
    }

    suspend fun getPendingFriends(userId: Long): List<FriendResponse> = try {
        friendshipsBy("senderId", userId)
    } catch (e: Exception) {
        Log.e(TAG, "getPendingFriends: ${e.message}", e); emptyList()
    }

    suspend fun getRequestFriends(userId: Long): List<FriendResponse> = try {
        friendshipsBy("receiverId", userId)
    } catch (e: Exception) {
        Log.e(TAG, "getRequestFriends: ${e.message}", e); emptyList()
    }

    suspend fun acceptedFriendRequest(friendshipId: Long): Boolean = try {
        firestore.collection(FirestoreCollections.FRIENDSHIPS)
            .document(friendshipId.toString())
            .update("status", STATUS_ACCEPTED).await()
        true
    } catch (e: Exception) {
        Log.e(TAG, "acceptedFriendRequest: ${e.message}", e); false
    }

    suspend fun cancelFriendRequest(friendshipId: Long): Boolean = try {
        firestore.collection(FirestoreCollections.FRIENDSHIPS)
            .document(friendshipId.toString())
            .delete().await()
        true
    } catch (e: Exception) {
        Log.e(TAG, "cancelFriendRequest: ${e.message}", e); false
    }
}
