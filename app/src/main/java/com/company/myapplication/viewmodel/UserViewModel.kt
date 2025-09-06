package com.company.myapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class UserViewModel(context: Context): ViewModel(){
    val repo = UserRepository(context = context)

    private val _userInfo = MutableStateFlow<UserResponse?>(null)
    val userInfo: StateFlow<UserResponse?> = _userInfo
    private val _userInfoList = MutableStateFlow<List<UserResponse>>(emptyList())
    val userInfoList: StateFlow<List<UserResponse>> = _userInfoList

    fun getUserInfo(userId: Long){
        viewModelScope.launch {
            val info = repo.getUserInfo(userId = userId)
            if (info != null){
                _userInfo.value = info
            }
        }
    }

    fun getUserInfoList(userIds: List<Long>?) {
        viewModelScope.launch {
            val result = mutableListOf<UserResponse>()
            userIds?.forEach { id ->
                val user = repo.getUserInfo(userId = id)
                if (user != null) {
                    result.add(user)
                }
            }
            _userInfoList.value = result
        }
    }
}