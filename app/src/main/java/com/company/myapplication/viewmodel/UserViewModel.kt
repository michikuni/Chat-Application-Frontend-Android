package com.company.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserResponse?>(null)
    val userInfo: StateFlow<UserResponse?> = _userInfo

    private val _userInfoList = MutableStateFlow<List<UserResponse>>(emptyList())
    val userInfoList: StateFlow<List<UserResponse>> = _userInfoList

    fun getUserInfo(userId: Long) {
        viewModelScope.launch {
            userRepository.getUserInfo(userId)?.let { _userInfo.value = it }
        }
    }

    fun getUserInfoList(userIds: List<Long>?) {
        viewModelScope.launch {
            val result = mutableListOf<UserResponse>()
            userIds?.forEach { id ->
                userRepository.getUserInfo(id)?.let { result.add(it) }
            }
            _userInfoList.value = result
        }
    }

    fun uploadImage(context: Context, uri: Uri, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            onResult(userRepository.uploadImage(context, uri))
        }
    }
}
