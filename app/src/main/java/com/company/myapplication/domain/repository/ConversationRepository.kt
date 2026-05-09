package com.company.myapplication.domain.repository

import android.content.Context
import android.net.Uri
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.CreateConversationGroup
import com.company.myapplication.data.model.chat.Message

interface ConversationRepository {
    suspend fun createMessage(userId: Long, body: CreateConversation): Boolean
    suspend fun findConversation(userId: Long, friendId: Long): Long
    suspend fun createConversationGroup(data: CreateConversationGroup, uri: Uri, context: Context)
    suspend fun getAllMessage(conversationId: Long): List<Message>?
    suspend fun getAllConversation(userId: Long): List<ConversationDTO>?
    suspend fun sendMediaFile(context: Context, uri: Uri, userId: Long, conversationId: Long)
    suspend fun updateTheme(context: Context, conversationId: Long, color: List<String>)
}
