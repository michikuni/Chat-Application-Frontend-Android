package com.company.myapplication.data.repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.company.myapplication.core.config.BackendSelector
import com.company.myapplication.core.config.BackendType
import com.company.myapplication.core.util.ToastHelper
import com.company.myapplication.data.firestore.datasource.ConversationFirestoreDataSource
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.CreateConversationGroup
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.data.remote.datasource.ConversationRemoteDataSource
import com.company.myapplication.domain.repository.ConversationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor(
    private val backendSelector: BackendSelector,
    private val remote: ConversationRemoteDataSource,
    private val firestore: ConversationFirestoreDataSource
) : ConversationRepository {
    override suspend fun createMessage(userId: Long, body: CreateConversation): Boolean =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.createMessage(userId, body)
            BackendType.FIRESTORE -> firestore.createMessage(userId, body)
        }

    override suspend fun findConversation(userId: Long, friendId: Long): Long =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.findConversation(userId, friendId)
            BackendType.FIRESTORE -> firestore.findConversation(userId, friendId)
        }

    override suspend fun createConversationGroup(data: CreateConversationGroup, uri: Uri, context: Context) {
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.createConversationGroup(data, uri, context)
            BackendType.FIRESTORE -> ToastHelper.showFeatureUnavailable(context)
        }
    }

    override suspend fun getAllMessage(conversationId: Long): List<Message>? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getAllMessage(conversationId)
            BackendType.FIRESTORE -> firestore.getAllMessage(conversationId)
        }

    override suspend fun getAllConversation(userId: Long): List<ConversationDTO>? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getAllConversation(userId)
            BackendType.FIRESTORE -> firestore.getAllConversation(userId)
        }

    override suspend fun sendMediaFile(context: Context, uri: Uri, userId: Long, conversationId: Long) {
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.sendMediaFile(context, uri, userId, conversationId)
            BackendType.FIRESTORE -> ToastHelper.showFeatureUnavailable(context)
        }
    }

    override suspend fun updateTheme(context: Context, conversationId: Long, color: List<String>) {
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.updateTheme(context, conversationId, color)
            BackendType.FIRESTORE -> {
                val ok = firestore.updateTheme(conversationId, color)
                ToastHelper.showOnMain(
                    context,
                    if (ok) "Cập nhật chủ đề thành công" else "Cập nhật chủ đề thất bại"
                )
            }
        }
    }
}
