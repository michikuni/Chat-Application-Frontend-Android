package com.company.myapplication.repository

import android.content.Context
import android.util.Log
import com.company.myapplication.data.api.AuthApi
import com.company.myapplication.data.api.ConversationApi
import com.company.myapplication.data.api.FcmApi
import com.company.myapplication.data.api.FriendApi
import com.company.myapplication.data.model.auth.LoginRequest
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.auth.RegisterRequest
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import com.company.myapplication.data.model.response.AuthResponse
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AuthRepository (context: Context){
    private fun createRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS) // tăng thời gian đọc
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    private val retrofit = createRetrofit(context)
    private val authApi = retrofit.create(AuthApi::class.java)

    suspend fun register(name: String, account: String, email:String, password: String): Boolean{
        val response = authApi.register(RegisterRequest(name, account, email, password))
        return response.isSuccessful
    }

    suspend fun login(account: String, password: String):LoginResponse?{
        val response = authApi.login(LoginRequest(account, password))
        return if (response.isSuccessful)
            response.body()
        else {
            val errorBody = response.errorBody()?.string()
            println("Login error: $errorBody")
            null
        }
    }

    suspend fun checkTokenValid(): AuthResponse? {
        val response = authApi.checkTokenValid()
        if (response.isSuccessful) {
            val body = response.body()
            Log.d("API", "Success body=$body")
            return body
        } else {
            Log.e("API", "Fail code=${response.code()}, error=${response.errorBody()?.string()}")
            return null
        }
    }


}