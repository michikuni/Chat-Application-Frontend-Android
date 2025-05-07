package com.company.myapplication.data.model.chat

data class BoxChat (
    val id: String,
    val name: String,
    val isOnline: Boolean,
    val content: String,
    val attachment: ChatContent,
    val avatarUrl: String,
    val timestamp: String
)