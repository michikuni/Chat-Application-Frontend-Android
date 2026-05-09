package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.chat.Conversation
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.data.model.response.UserResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.sql.Timestamp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private companion object { const val TAG = "ConversationFirestore" }

    suspend fun createMessage(userId: Long, body: CreateConversation): Boolean = try {
        val messageId = System.currentTimeMillis()
        val now = System.currentTimeMillis()
        firestore.collection(FirestoreCollections.MESSAGES)
            .document(messageId.toString())
            .set(
                mapOf(
                    "id" to messageId,
                    "conversationId" to body.conversationId,
                    "senderId" to userId,
                    "content" to body.message,
                    "mediaFile" to null,
                    "createdAt" to now,
                    "isRead" to false
                )
            ).await()

        firestore.collection(FirestoreCollections.CONVERSATIONS)
            .document(body.conversationId.toString())
            .update(
                mapOf(
                    "lastContent" to body.message,
                    "lastSenderId" to userId,
                    "lastCreatedAt" to now
                )
            ).await()
        true
    } catch (e: Exception) {
        Log.e(TAG, "createMessage: ${e.message}", e); false
    }

    suspend fun findConversation(userId: Long, friendId: Long): Long = try {
        val snapshot = firestore.collection(FirestoreCollections.CONVERSATIONS)
            .whereEqualTo("type", "PAIR")
            .whereArrayContains("memberIds", userId)
            .get().await()
        val match = snapshot.documents.firstOrNull { doc ->
            val members = doc.get("memberIds") as? List<*> ?: emptyList<Any>()
            members.contains(friendId)
        }
        match?.getLong("id") ?: -1L
    } catch (e: Exception) {
        Log.e(TAG, "findConversation: ${e.message}", e); -1L
    }

    suspend fun getAllMessage(conversationId: Long): List<Message>? = try {
        val snapshot = firestore.collection(FirestoreCollections.MESSAGES)
            .whereEqualTo("conversationId", conversationId)
            .get().await()

        val senderIds = snapshot.documents.mapNotNull { it.getLong("senderId") }.distinct()
        val users = if (senderIds.isEmpty()) emptyMap() else
            firestore.collection(FirestoreCollections.USERS)
                .whereIn("id", senderIds.take(10))
                .get().await()
                .documents.associateBy({ it.getLong("id") ?: 0L }) {
                    UserResponse(
                        id = it.getLong("id") ?: 0L,
                        name = it.getString("name") ?: "",
                        username = it.getString("username") ?: "",
                        email = it.getString("email") ?: "",
                        avatar = it.getString("avatar")
                    )
                }

        snapshot.documents.map { doc ->
            val senderId = doc.getLong("senderId") ?: 0L
            val createdAtMs = doc.getLong("createdAt") ?: System.currentTimeMillis()
            Message(
                conversationId = Conversation(
                    id = conversationId,
                    memberIds = emptyList(),
                    conversationName = null,
                    avatar = null,
                    numberMembers = 0,
                    createdAt = Timestamp(createdAtMs)
                ),
                senderId = users[senderId] ?: UserResponse(senderId, "", "", "", null),
                content = doc.getString("content"),
                mediaFile = doc.getString("mediaFile"),
                createdAt = Timestamp(createdAtMs),
                isRead = doc.getBoolean("isRead") ?: false
            )
        }.sortedBy { it.createdAt.time }
    } catch (e: Exception) {
        Log.e(TAG, "getAllMessage: ${e.message}", e); null
    }

    suspend fun getAllConversation(userId: Long): List<ConversationDTO>? = try {
        val snapshot = firestore.collection(FirestoreCollections.CONVERSATIONS)
            .whereArrayContains("memberIds", userId)
            .get().await()

        snapshot.documents.map { doc ->
            val memberIds = (doc.get("memberIds") as? List<*>)?.joinToString(",") ?: ""
            val createdAtMs = doc.getLong("createdAt")
            val lastCreatedMs = doc.getLong("lastCreatedAt")
            ConversationDTO(
                id = doc.getLong("id") ?: 0L,
                groupAvatar = doc.getString("groupAvatar"),
                conversationName = doc.getString("conversationName"),
                themeColor = doc.getString("themeColor"),
                conversationType = doc.getString("type"),
                membersIds = memberIds,
                name = doc.getString("conversationName") ?: "",
                pairAvatar = doc.getString("pairAvatar"),
                content = doc.getString("lastContent"),
                mediaFile = null,
                senderId = doc.getLong("lastSenderId"),
                createdAt = createdAtMs?.let { Timestamp(it) },
                csCreatedAt = lastCreatedMs?.let { Timestamp(it) },
                isRead = doc.getBoolean("isRead")
            )
        }
    } catch (e: Exception) {
        Log.e(TAG, "getAllConversation: ${e.message}", e); null
    }

    suspend fun updateTheme(conversationId: Long, color: List<String>): Boolean = try {
        firestore.collection(FirestoreCollections.CONVERSATIONS)
            .document(conversationId.toString())
            .update("themeColor", color.firstOrNull()).await()
        true
    } catch (e: Exception) {
        Log.e(TAG, "updateTheme: ${e.message}", e); false
    }
}
