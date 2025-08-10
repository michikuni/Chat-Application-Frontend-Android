package com.company.myapplication.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.company.myapplication.data.api.ApiService
import com.company.myapplication.data.model.auth.LoginRequest
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.auth.RegisterRequest
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.data.model.chat.GetConversation
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.data.model.fcm.fcmTokenResponse
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
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
            .baseUrl("http://10.0.2.2:8080")
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

    suspend fun getAllFriendsById(userId: Long): List<UserResponse> {
        val response = api.getAllFriendsById(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách bạn bè: ${response.code()}")
        }
    }

    suspend fun sendAddRequest(userId: Long, receiverEmail: RequestBody): Boolean{
        val response = api.sendAddRequest(userId, receiverEmail)
        return response.isSuccessful
    }

    suspend fun getPendingFriends(userId: Long): List<FriendResponse>{
        val response = api.getPendingFriends(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách yêu cầu kết bè: ${response.code()}")
        }
    }

    suspend fun getRequestFriends(userId: Long): List<FriendResponse>{
        val response = api.getRequestFriends(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách yêu cầu kết bè: ${response.code()}")
        }
    }

    suspend fun acceptedFriendRequest(friendshipId: Long): Boolean{
        val response = api.acceptedFriendRequest(friendshipId)
        if (response.isSuccessful){
            return response.isSuccessful
        } else {
            throw IllegalArgumentException(response.body().toString())
        }
    }
    suspend fun createConversation(userId: Long, body: CreateConversation): Boolean {
        val response = api.createConversation(userId, body)
        return response.isSuccessful
    }

    suspend fun getAllMessage(userId: Long, friendId: Long): List<Message>? {
        val response = api.getAllMessage(userId, friendId)
        return if (response.isSuccessful){
            response.body()
        } else {
            throw Exception("Lỗi lấy tin nhắn: ${response.code()}")
        }
    }

    suspend fun getAllConversation(userId: Long): List<GetConversation>? {
        val response = api.getAllConversation(userId)
        return if (response.isSuccessful){
            response.body()
        } else {
            throw Exception("Lỗi lấy hội thoại: ${response.code()}")
        }
    }

    suspend fun cancelFriendRequest(friendshipId: Long): Boolean{
        val response = api.cancelFriendRequest(friendshipId)
        if (response.isSuccessful){
            return response.isSuccessful
        } else {
            throw IllegalArgumentException(response.body().toString())
        }
    }

    suspend fun sendToken(fcmTokenResponse: fcmTokenResponse): Boolean {
        val response = api.sendTokenFcm(fcmTokenResponse)
        return if (response.isSuccessful) {
            true
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
            Log.e("FCM", "Send token failed: HTTP ${response.code()} - $errorMsg")
            false
        }
    }

}