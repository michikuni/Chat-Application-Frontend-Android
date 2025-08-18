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

    private val _user_info = MutableStateFlow<UserResponse?>(null)
    val user_info: StateFlow<UserResponse?> = _user_info

    fun getUserInfo(userId: Long){
        viewModelScope.launch {
            val info = repo.getUserInfo(userId = userId)
            if (info != null){
                _user_info.value = info
            }
        }
    }
}