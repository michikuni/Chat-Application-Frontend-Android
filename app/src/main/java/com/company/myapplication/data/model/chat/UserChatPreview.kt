package com.company.myapplication.data.model.chat

data class UserChatPreview(
    val id: Long,
    val name: String,
    val lastMessage: String,
    val time: String,
    val avatarUrl: String,
    val isOnline: Boolean
)
