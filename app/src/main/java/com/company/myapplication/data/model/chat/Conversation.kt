package com.company.myapplication.data.model.chat

import java.sql.Timestamp

data class CreateConversation(
    val conversationId: Long,
    val message: String
)

data class GetConversation(
    var id: Long,
    val userId: Long,
    val senderId: Long,
    val name: String,
    val conversationName: String?,
    val avatar: String?,
    val content: String?,
    val mediaFile: String?,
    val createdAt: Timestamp,
    val isRead: Boolean,
    val themeColor: String?,
    val conversationType: String
)

data class CreateConversationGroup(
    val members: List<Long>,
    val name: String
)

data class Conversation(
    val id: Long,
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val createdAt: Timestamp
)
