package com.company.myapplication.data.remote.datasource

import com.company.myapplication.data.api.AuthApi
import com.company.myapplication.data.model.auth.LoginRequest
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.auth.RegisterRequest
import com.company.myapplication.data.model.response.AuthResponse
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun login(account: String, password: String): LoginResponse? {
        val response = authApi.login(LoginRequest(account, password))
        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody ?: "").optString("message", "Đăng nhập thất bại")
            } catch (e: Exception) {
                "Đăng nhập thất bại"
            }
            throw IllegalArgumentException(errorMessage)
        }
    }

    suspend fun register(name: String, account: String, email: String, password: String): Boolean {
        val response = authApi.register(RegisterRequest(name, account, email, password))
        return if (response.isSuccessful) {
            true
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                JSONObject(errorBody ?: "").optString("message", "Đăng ký thất bại")
            } catch (e: Exception) {
                "Đăng ký thất bại"
            }
            throw IllegalArgumentException(errorMessage)
        }
    }

    suspend fun checkTokenValid(): AuthResponse? {
        val response = authApi.checkTokenValid()
        return if (response.isSuccessful) response.body() else null
    }
}
