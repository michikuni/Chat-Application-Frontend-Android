package com.company.myapplication.domain.repository

import android.content.Context
import android.net.Uri
import com.company.myapplication.data.model.response.UserResponse

interface UserRepository {
    suspend fun uploadImage(context: Context, uri: Uri): Boolean
    suspend fun getUserInfo(userId: Long): UserResponse?
}
