package com.company.myapplication.data.model.response

data class FriendResponse (
    val friendshipId: Long,
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null
)