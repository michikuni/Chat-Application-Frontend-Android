package com.company.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.myapplication.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val repo = AuthRepository()

    var token by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var registerSuccess by mutableStateOf(false)

    fun login(username: String, password: String){
        viewModelScope.launch {
            try {
                val result = repo.login(username, password)

                if (result != null){
                    token = result
                    errorMessage = null
                } else{
                    errorMessage = "Login failed can't response token"
                }
            } catch (e: Exception){
                errorMessage = "Failed exception token: ${e.message}"
            }
        }
    }

    fun register(name: String, username: String, email: String, password: String){
        viewModelScope.launch {
            try {
                val success = repo.register(name, username, email, password)
                registerSuccess = success
                errorMessage = if (success) null else "Register failed"
            } catch (e: Exception){
                errorMessage = "Failed register: ${e.message}"
                registerSuccess = false
            }
        }
    }
}