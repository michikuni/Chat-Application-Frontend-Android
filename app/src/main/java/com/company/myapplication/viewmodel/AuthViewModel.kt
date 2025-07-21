package com.company.myapplication.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.user.UserRespone
import com.company.myapplication.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(activity: Activity): ViewModel(){
    private val repo = AuthRepository(activity)

    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult.asStateFlow()
    var errorMessage by mutableStateOf<String?>(null)
    var registerSuccess by mutableStateOf(false)

    var userId: Long? = null
        private set

    private val _friends = MutableStateFlow<List<UserRespone>>(emptyList())
    val friends: StateFlow<List<UserRespone>> = _friends.asStateFlow()

    fun login(account: String, password: String){
        viewModelScope.launch {
            try {
                val result = repo.login(account, password)

                if (result != null){
                    _loginResult.value = result
                    userId = result.id
                    errorMessage = null
                } else{
                    errorMessage = "Login failed can't response token"
                }
            } catch (e: Exception){
                errorMessage = "Failed exception token: ${e.message}"
            }
        }
    }

    fun register(name: String, account: String, email: String, password: String){
        viewModelScope.launch {
            try {
                val success = repo.register(name, account, email, password)
                registerSuccess = success
                errorMessage = if (success) null else "Register failed"
            } catch (e: Exception){
                errorMessage = "Failed register: ${e.message}"
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
}