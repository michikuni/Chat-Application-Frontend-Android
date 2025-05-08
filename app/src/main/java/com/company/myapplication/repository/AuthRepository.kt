package com.company.myapplication.repository

import com.company.myapplication.data.api.ApiService
import com.company.myapplication.data.model.request.LoginRequest
import com.company.myapplication.data.model.request.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.5.106:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    suspend fun register(name: String, username: String, email:String, password: String): Boolean{
        val response = api.register(RegisterRequest(name, username, email, password))
        return response.isSuccessful
    }

    suspend fun login(username: String, password: String):String?{
        val response = api.login(LoginRequest(username, password))
        return if (response.isSuccessful) response.body()?.token else {
            // Log lỗi chi tiết
            val errorBody = response.errorBody()?.string()
            println("Login error: $errorBody")
            null
        }
    }
}