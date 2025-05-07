package com.company.myapplication.data.model.chat

sealed class ChatContent {
    data class Text(val message: String) : ChatContent()
    data class Image(val imageUrl : String) : ChatContent()
}