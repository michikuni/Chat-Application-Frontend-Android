package com.company.myapplication.data.model.chat

import com.company.myapplication.data.model.response.UserResponse
import java.time.Instant

data class Message (
    val conversationId: Conversation,
    val senderId: UserResponse,
    val content: String,
    val createdAt: Instant,
    val isRead: Boolean
)