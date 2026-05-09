package com.company.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val repo: FriendRepository
) : ViewModel() {

    var errorMsg by mutableStateOf<String?>(null)

    private val _allFriends = MutableStateFlow<List<UserResponse>>(emptyList())
    val allFriends: StateFlow<List<UserResponse>> = _allFriends

    var sendAddFriendSuccess by mutableStateOf<String?>(null)
    var acceptedFriendSuccess by mutableStateOf(false)
    var canceledFriendSuccess by mutableStateOf(false)

    private val _friendInfo = MutableStateFlow<UserResponse?>(null)
    val friendInfo: StateFlow<UserResponse?> get() = _friendInfo

    private val _pendingFriends = MutableStateFlow<List<FriendResponse>>(emptyList())
    val pendingFriends: StateFlow<List<FriendResponse>> get() = _pendingFriends

    private val _requestFriends = MutableStateFlow<List<FriendResponse>>(emptyList())
    val requestFriends: StateFlow<List<FriendResponse>> get() = _requestFriends

    fun getAllFriendsById(userId: Long) {
        viewModelScope.launch {
            try {
                _allFriends.value = repo.getAllFriendsById(userId)
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    fun getFriendByEmail(email: String) {
        viewModelScope.launch {
            try {
                _friendInfo.value = repo.getFriendByEmail(email)
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    fun sendAddRequest(userId: Long, receiverEmail: String) {
        viewModelScope.launch {
            try {
                val responseMessage = repo.sendAddRequest(userId, receiverEmail)
                if (!responseMessage.isNullOrBlank()) {
                    sendAddFriendSuccess = responseMessage
                    errorMsg = null
                } else {
                    errorMsg = "Phản hồi rỗng từ máy chủ"
                }
            } catch (e: HttpException) {
                errorMsg = "Lỗi server: ${e.code()} - ${e.message()}"
            } catch (e: IOException) {
                errorMsg = "Không thể kết nối tới máy chủ. Vui lòng kiểm tra mạng."
            } catch (e: Exception) {
                errorMsg = "Lỗi không xác định: ${e.message}"
            }
        }
    }

    fun acceptedFriendRequest(friendshipId: Long) {
        viewModelScope.launch {
            try {
                acceptedFriendSuccess = repo.acceptedFriendRequest(friendshipId)
            } catch (e: IllegalArgumentException) {
                errorMsg = e.message
            }
        }
    }

    fun cancelFriendRequest(friendshipId: Long) {
        viewModelScope.launch {
            try {
                canceledFriendSuccess = repo.cancelFriendRequest(friendshipId)
            } catch (e: IllegalArgumentException) {
                errorMsg = e.message
            }
        }
    }

    fun getPendingFriendRequest(userId: Long) {
        viewModelScope.launch {
            try {
                _pendingFriends.value = repo.getPendingFriends(userId)
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    fun getRequestFriendRequest(userId: Long) {
        viewModelScope.launch {
            try {
                _requestFriends.value = repo.getRequestFriends(userId)
            } catch (e: Exception) {
                errorMsg = e.message
            }
        }
    }

    fun resetFriendInfo() {
        sendAddFriendSuccess = null
        errorMsg = null
    }
}
