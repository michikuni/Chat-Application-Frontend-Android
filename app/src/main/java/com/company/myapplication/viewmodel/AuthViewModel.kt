package com.company.myapplication.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import com.company.myapplication.repository.AuthRepository
import com.company.myapplication.repository.FcmRepository
import com.company.myapplication.util.UserSharedPreferences
import kotlinx.coroutines.launch

class AuthViewModel(activity: Activity): ViewModel(){
    private val repo = AuthRepository(activity)
    private val fcmRepo = FcmRepository(activity)

    var errorMessage by mutableStateOf<String?>(null)

    var registerSuccess by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false)

    fun login(activity: Activity, account: String, password: String){
        viewModelScope.launch {
            try {
                UserSharedPreferences.clearSession(activity)
                val result = repo.login(account, password)
                val fcmToken = UserSharedPreferences.getFcmToken(activity)
                Log.d("FCM", "Send token success: $result")
                if (result != null){
                    val id = result.id
                    val username = result.username
                    val token = result.token
                    errorMessage = null
                    fcmToken?.let { FcmTokenResponse(userId = id, token = it) }
                        ?.let { fcmRepo.sendToken(it) }
                    UserSharedPreferences.saveUser(activity, id = id, username = username, token = token)
                    loginSuccess = true
                } else{
                    errorMessage = "Login failed can't response token"
                }
            } catch (e: NullPointerException){
                errorMessage = "Lỗi đăng nhập ${e.message}"
            } catch (e: Exception){
                errorMessage = "Lỗi đăng nhập ${e.message}"
            }
        }
    }

    fun logout(activity: Activity) {
        UserSharedPreferences.clearSession(activity)
    }


    fun register(name: String, account: String, email: String, password: String){
        viewModelScope.launch {
            try {
                val success = repo.register(name, account, email, password)
                registerSuccess = success
                errorMessage = if (success) null else "Đăng ký thất bại"
            } catch (e: IllegalArgumentException){
                errorMessage = "Lỗi đăng ký: ${e.message}"
                registerSuccess = false
            }
        }
    }
}