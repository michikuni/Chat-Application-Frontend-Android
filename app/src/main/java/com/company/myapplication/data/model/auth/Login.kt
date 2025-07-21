package com.company.myapplication.data.model.auth

data class LoginRequest (
    val username: String,
    val password: String
)
data class LoginResponse(
    val id: Long,
    val username: String,
    val token: String
)
