package com.company.myapplication.data.model.response

data class AuthResponse (
    val valid: Boolean,
    val username: String? = null
)