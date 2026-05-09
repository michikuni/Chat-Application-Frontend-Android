package com.company.myapplication.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import com.company.myapplication.domain.repository.AuthRepository
import com.company.myapplication.domain.repository.FcmRepository
import com.company.myapplication.util.UserSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val fcmRepository: FcmRepository
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
    var registerSuccess by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false)

    fun login(activity: Activity, account: String, password: String) {
        viewModelScope.launch {
            try {
                UserSharedPreferences.clearSession(activity)
                val result = authRepository.login(account, password)
                val fcmToken = UserSharedPreferences.getFcmToken(activity)

                if (result != null) {
                    fcmToken?.let { FcmTokenResponse(userId = result.id, token = it) }
                        ?.let { fcmRepository.sendToken(it) }

                    UserSharedPreferences.saveUser(activity, result.id, result.username, result.token)
                    loginSuccess = true
                    errorMessage = null
                }
            } catch (e: IllegalArgumentException) {
                errorMessage = e.message ?: "Tên đăng nhập hoặc mật khẩu không đúng"
            } catch (e: Exception) {
                errorMessage = "Lỗi đăng nhập: ${e.message}"
            }
        }
    }

    fun logout(activity: Activity) {
        authRepository.logout()
        UserSharedPreferences.clearSession(activity)
    }

    fun register(name: String, account: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val success = authRepository.register(name, account, email, password)
                registerSuccess = success
                errorMessage = if (success) null else "Đăng ký thất bại"
            } catch (e: IllegalArgumentException) {
                errorMessage = "Lỗi đăng ký: ${e.message}"
                registerSuccess = false
            }
        }
    }

    fun resetErrorMessage() {
        errorMessage = null
    }
}
