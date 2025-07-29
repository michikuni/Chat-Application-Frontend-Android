package com.company.myapplication.data.model.chat

import com.company.myapplication.data.model.response.UserResponse
import java.sql.Timestamp

data class Message (
    val conversationId: Conversation,
    val senderId: UserResponse,
    val content: String,
    val createdAt: Timestamp,
    val isRead: Boolean
)