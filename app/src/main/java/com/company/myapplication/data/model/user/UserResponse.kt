package com.company.myapplication.data.model.user

import java.time.Instant

data class UserResponse(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null
)