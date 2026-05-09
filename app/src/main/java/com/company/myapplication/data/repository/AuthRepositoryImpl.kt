package com.company.myapplication.data.repository

import android.content.Context
import com.company.myapplication.core.config.BackendSelector
import com.company.myapplication.core.config.BackendType
import com.company.myapplication.data.firestore.datasource.AuthFirebaseAuthDataSource
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.response.AuthResponse
import com.company.myapplication.data.remote.datasource.AuthRemoteDataSource
import com.company.myapplication.domain.repository.AuthRepository
import com.company.myapplication.util.UserSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val backendSelector: BackendSelector,
    private val remote: AuthRemoteDataSource,
    private val firebaseAuth: AuthFirebaseAuthDataSource
) : AuthRepository {
    override suspend fun login(account: String, password: String): LoginResponse? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.login(account, password)
            BackendType.FIRESTORE -> firebaseAuth.login(account, password)
        }

    override suspend fun register(name: String, account: String, email: String, password: String): Boolean =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.register(name, account, email, password)
            BackendType.FIRESTORE -> firebaseAuth.register(name, account, email, password)
        }

    override suspend fun checkTokenValid(): AuthResponse? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.checkTokenValid()
            BackendType.FIRESTORE -> firebaseAuth.checkTokenValid(UserSharedPreferences.getToken(context))
        }

    override fun logout() {
        if (backendSelector.current() == BackendType.FIRESTORE) {
            firebaseAuth.logout()
        }
    }
}
