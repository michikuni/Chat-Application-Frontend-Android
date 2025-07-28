package com.company.myapplication.data.model.chat

import java.time.Instant

data class CreateConversation(
    val friendId: Long,
    val message: String
)

data class GetConversation(
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val message: List<Message>,
    val createdAt: Instant
)
data class Conversation(
    val id: Long,
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val message: List<Message>,
    val createdAt: Instant
)
