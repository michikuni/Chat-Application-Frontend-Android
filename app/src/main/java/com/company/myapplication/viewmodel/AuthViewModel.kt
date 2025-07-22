package com.company.myapplication.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.company.myapplication.data.model.user.UserResponse
import com.company.myapplication.repository.AuthRepository
import com.company.myapplication.util.UserSharedPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class AuthViewModel(activity: Activity): ViewModel(){
    private val repo = AuthRepository(activity)

    var errorMessage by mutableStateOf<String?>(null)
    var registerSuccess by mutableStateOf(false)
    var sendAddSuccess by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false)


    private val _friends = MutableStateFlow<List<UserResponse>>(emptyList())
    val friends: StateFlow<List<UserResponse>> get() = _friends


    fun login(activity: Activity, account: String, password: String){
        viewModelScope.launch {
            try {
                UserSharedPreferences.clearSession(activity)
                val result = repo.login(account, password)
                if (result != null){
                    val id = result.id
                    val username = result.username
                    val token = result.token
                    errorMessage = null

                    Log.e("Auth ViewModel", "ID: $id, USERNAME $username, TOKEN $token")
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

    fun getAllFriends(userId: Long){
        viewModelScope.launch {
            Log.e("AVM", "ID $userId")
            try {
                val friendList = repo.getAllFriendsById(userId)
                _friends.value = friendList
                for (fr in friendList){
                    Log.e("AVM", "Tên: ${fr.name}")
                }
            } catch (e: Exception){
                errorMessage = "Lỗi lấy danh sách bạn bè ${e.message}"
            }
        }
    }

    fun sendAddRequest(userId: Long, receiverEmail: RequestBody){
        viewModelScope.launch {
            try {
                val success = repo.sendAddRequest(userId, receiverEmail)
                sendAddSuccess = success
                Log.e("SENDDDD", "$sendAddSuccess")
                errorMessage = if (success) null else "Gửi lời mời kết bạn thất bại"
            } catch (e: IllegalArgumentException){
                errorMessage = "Lỗi gửi lời mời kết bạn${e.message}"
            }

        }
    }
}