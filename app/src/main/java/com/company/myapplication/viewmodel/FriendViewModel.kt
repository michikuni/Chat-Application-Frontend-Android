package com.company.myapplication.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class FriendViewModel (activity: Activity): ViewModel(){
    private val repo = FriendRepository(activity)

    var errorMsg by mutableStateOf<String?>(null)

    private val _allFriends = MutableStateFlow<List<UserResponse>>(emptyList())
    val allFriends: StateFlow<List<UserResponse>> = _allFriends

    var sendAddFriendSuccess by mutableStateOf(false)

    var acceptedFriendSuccess by mutableStateOf(false)

    var canceledFriendSuccess by mutableStateOf(false)

    private val _pendingFriends = MutableStateFlow<List<FriendResponse>>(emptyList())
    val pendingFriends: StateFlow<List<FriendResponse>> get() = _pendingFriends

    private val _requestFriends = MutableStateFlow<List<FriendResponse>>(emptyList())
    val requestFriends: StateFlow<List<FriendResponse>> get() = _requestFriends

    fun getAllFriendsById(userId: Long){
        viewModelScope.launch {
            try {
                val friends = repo.getAllFriendsById(userId = userId)
                _allFriends.value = friends
            } catch (e: Exception){
                errorMsg = e.message
            }
        }
    }

    fun sendAddRequest(userId: Long, receiverEmail: RequestBody){
        viewModelScope.launch {
            try {
                val sendAdd = repo.sendAddRequest(userId = userId, receiverEmail = receiverEmail)
                sendAddFriendSuccess = sendAdd
                errorMsg = if (sendAdd) null else "Gửi lời mời kết bạn thất bại"
            } catch (e: IllegalArgumentException){
                errorMsg = e.message
            }
        }
    }

    fun acceptedFriendRequest(friendshipId: Long){
        viewModelScope.launch {
            try {
                val acceptedSuccess = repo.acceptedFriendRequest(friendshipId = friendshipId)
                acceptedFriendSuccess = acceptedSuccess
            } catch (e: IllegalArgumentException){
                errorMsg = e.message
            }
        }
    }

    fun cancelFriendRequest(friendshipId: Long){
        viewModelScope.launch {
            try {
                val cancelFriendSuccess = repo.cancelFriendRequest(friendshipId = friendshipId)
                canceledFriendSuccess = cancelFriendSuccess
            } catch (e: IllegalArgumentException){
                errorMsg = e.message
            }
        }
    }

    fun getPendingFriendRequest(userId: Long){
        viewModelScope.launch {
            try {
                val pendingFriends = repo.getPendingFriends(userId = userId)
                _pendingFriends.value = pendingFriends
            } catch (e: Exception){
                errorMsg = e.message
            }
        }
    }

    fun getRequestFriendRequest(userId: Long){
        viewModelScope.launch {
            try {
                val friendsRequest = repo.getRequestFriends(userId = userId)
                _requestFriends.value = friendsRequest
            } catch (e: Exception){
                errorMsg = e.message
            }
        }
    }
}