package com.company.myapplication.data.model.chat

import java.sql.Timestamp

data class CreateConversation(
    val conversationId: Long,
    val message: String
)

data class ConversationDTO(
    val id: Long,
    val groupAvatar: String?,
    val conversationName: String?,
    val themeColor: String?,
    val conversationType: String?,
    val membersIds: String?,
    val name: String,
    val pairAvatar: String?,
    val content: String?,
    val mediaFile: String?,
    val senderId: Long?,
    val createdAt: Timestamp?,
    val isRead: Boolean?,
)

data class ConversationViewDTO(
    val id: Long,
    val groupAvatar: String?,
    val conversationName: String?,
    val themeColor: String?,
    val conversationType: String?,
    val membersIds: List<Long>,
    val name: List<String>,
    val pairAvatar: List<String>,
    val content: String?,
    val mediaFile: String?,
    val senderId: Long?,
    val createdAt: Timestamp?,
    val isRead: Boolean?,
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
