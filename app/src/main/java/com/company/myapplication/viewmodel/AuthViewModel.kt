package com.company.myapplication.viewmodel

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
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
    var acceptedSuccess by mutableStateOf(false)
    var canceledSuccess by mutableStateOf(false)
    var loginSuccess by mutableStateOf(false)
    private var createConversationSuccess by mutableStateOf(false)

    private val _friends = MutableStateFlow<List<UserResponse>>(emptyList())
    val friends: StateFlow<List<UserResponse>> get() = _friends

    private val _friendsPending = MutableStateFlow<List<FriendResponse>>(emptyList())
    val friendsPending: StateFlow<List<FriendResponse>> get() = _friendsPending

    private val _friendsRequest = MutableStateFlow<List<FriendResponse>>(emptyList())
    val friendsRequest: StateFlow<List<FriendResponse>> get() = _friendsRequest

    private val _message = MutableStateFlow<List<Message>>(emptyList())
    val message: StateFlow<List<Message>> get() = _message

    private val _conversation = MutableStateFlow<List<GetConversation>>(emptyList())
    val conversation: StateFlow<List<GetConversation>> get() = _conversation

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
            try {
                val friendList = repo.getAllFriendsById(userId)
                _friends.value = friendList
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
                errorMessage = if (success) null else "Gửi lời mời kết bạn thất bại"
            } catch (e: IllegalArgumentException){
                errorMessage = "Lỗi gửi lời mời kết bạn${e.message}"
            }

        }
    }

    fun getPendingFriends(userId: Long){
        viewModelScope.launch {
            try {
                val friendPending = repo.getPendingFriends(userId)
                _friendsPending.value = friendPending
            } catch (e: Exception){
                errorMessage = "Lỗi lấy danh sách bạn bè ${e.message}"
            }
        }
    }

    fun getRequestFriends(userId: Long){
        viewModelScope.launch {
            try {
                val friendRequest = repo.getRequestFriends(userId)
                _friendsRequest.value = friendRequest
            } catch (e: Exception){
                errorMessage = "Lỗi lấy danh sách bạn bè ${e.message}"
            }
        }
    }

    fun acceptedFriendRequest(friendshipId: Long){
        viewModelScope.launch {
            try {
                val success = repo.acceptedFriendRequest(friendshipId)
                acceptedSuccess = success
            } catch (e: IllegalArgumentException){
                errorMessage = "Lỗi chấp nhận lời mời kết bạn ${e.message}"
            }
        }
    }

    fun canceledFriendRequest(friendshipId: Long){
        viewModelScope.launch {
            try {
                val success = repo.cancelFriendRequest(friendshipId)
                canceledSuccess = success
            } catch (e: IllegalArgumentException){
                errorMessage = "Lỗi hủy kết bạn ${e.message}"
            }
        }
    }

    fun createConversation(userId: Long, body: CreateConversation){
        viewModelScope.launch {
            val success = repo.createConversation(userId, body)
            createConversationSuccess = success
        }
    }

    fun getAllMessage(userId: Long, friendId: Long){
        viewModelScope.launch {
            try {
                val allMessage = repo.getAllMessage(userId, friendId)
                if (allMessage != null) {
                    _message.value = allMessage
                }
            } catch (e: Exception){
                errorMessage = e.message
            }
        }
    }

    fun getAllConversation(userId: Long){
        viewModelScope.launch {
            try {
                val allConversation = repo.getAllConversation(userId)
                if (allConversation != null){
                    _conversation.value = allConversation
                }
            } catch (e: Exception){
                errorMessage = e.message
            }
        }
    }
}