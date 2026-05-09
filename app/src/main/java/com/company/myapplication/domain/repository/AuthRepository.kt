package com.company.myapplication.domain.repository

import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.response.AuthResponse

interface AuthRepository {
    suspend fun login(account: String, password: String): LoginResponse?
    suspend fun register(name: String, account: String, email: String, password: String): Boolean
    suspend fun checkTokenValid(): AuthResponse?
    fun logout()
}
