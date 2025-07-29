package com.company.myapplication.data.model.chat

import java.sql.Timestamp

data class CreateConversation(
    val friendId: Long,
    val message: String
)

data class GetConversation(
    val id: Long,
    val userId: Long,
    val name: String,
    val avatar: String?,
    val content: String,
    val createdAt: Timestamp,
    val isRead: Boolean
)
data class Conversation(
    val id: Long,
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val createdAt: Timestamp
)
