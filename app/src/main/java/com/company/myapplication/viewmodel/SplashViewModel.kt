package com.company.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.company.myapplication.data.model.response.AuthResponse
import com.company.myapplication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun checkTokenValid(): AuthResponse? = authRepository.checkTokenValid()
}
