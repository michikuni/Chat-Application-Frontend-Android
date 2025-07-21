package com.company.myapplication.repository

import android.app.Activity
import com.company.myapplication.data.api.ApiService
import com.company.myapplication.data.model.auth.LoginRequest
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.auth.RegisterRequest
import com.company.myapplication.data.model.user.UserRespone
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository (context: Activity){
    private fun createRetrofit(context: Activity): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.5.100:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    private val retrofit = createRetrofit(context)
    private val api = retrofit.create(ApiService::class.java)

    suspend fun register(name: String, account: String, email:String, password: String): Boolean{
        val response = api.register(RegisterRequest(name, account, email, password))
        return response.isSuccessful
    }

    suspend fun login(account: String, password: String):LoginResponse?{
        val response = api.login(LoginRequest(account, password))
        return if (response.isSuccessful)
            response.body()
        else {
            val errorBody = response.errorBody()?.string()
            println("Login error: $errorBody")
            null
        }
    }

    suspend fun getAllFriendsById(userId: Long): List<UserRespone> {
        val response = api.getAllFriendsById(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách bạn bè: ${response.code()}")
        }
    }
}