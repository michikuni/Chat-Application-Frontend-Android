package com.company.myapplication.data.repository

import android.content.Context
import android.net.Uri
import com.company.myapplication.core.config.BackendSelector
import com.company.myapplication.core.config.BackendType
import com.company.myapplication.core.util.ToastHelper
import com.company.myapplication.data.firestore.datasource.UserFirestoreDataSource
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.data.remote.datasource.UserRemoteDataSource
import com.company.myapplication.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val backendSelector: BackendSelector,
    private val remote: UserRemoteDataSource,
    private val firestore: UserFirestoreDataSource
) : UserRepository {
    override suspend fun uploadImage(context: Context, uri: Uri): Boolean =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.uploadImage(context, uri)
            BackendType.FIRESTORE -> {
                ToastHelper.showFeatureUnavailable(context)
                false
            }
        }

    override suspend fun getUserInfo(userId: Long): UserResponse? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getUserInfo(userId)
            BackendType.FIRESTORE -> firestore.getUserInfo(userId)
        }
}
