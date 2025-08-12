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
    private val friendApi = retrofit.create(FriendApi::class.java)
    private val conversationApi = retrofit.create(ConversationApi::class.java)
    private val fcmApi = retrofit.create(FcmApi::class.java)


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

    suspend fun getAllFriendsById(userId: Long): List<UserResponse> {
        val response = friendApi.getAllFriendsById(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách bạn bè: ${response.code()}")
        }
    }

    suspend fun sendAddRequest(userId: Long, receiverEmail: RequestBody): Boolean{
        val response = friendApi.sendAddRequest(userId, receiverEmail)
        return response.isSuccessful
    }

    suspend fun getPendingFriends(userId: Long): List<FriendResponse>{
        val response = friendApi.getPendingFriends(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách yêu cầu kết bè: ${response.code()}")
        }
    }

    suspend fun getRequestFriends(userId: Long): List<FriendResponse>{
        val response = friendApi.getRequestFriends(userId)
        if (response.isSuccessful){
            return response.body() ?: emptyList()
        } else{
            throw Exception("Lỗi lấy danh sách yêu cầu kết bè: ${response.code()}")
        }
    }

    suspend fun acceptedFriendRequest(friendshipId: Long): Boolean{
        val response = friendApi.acceptedFriendRequest(friendshipId)
        if (response.isSuccessful){
            return response.isSuccessful
        } else {
            throw IllegalArgumentException(response.body().toString())
        }
    }

    suspend fun cancelFriendRequest(friendshipId: Long): Boolean{
        val response = friendApi.cancelFriendRequest(friendshipId)
        if (response.isSuccessful){
            return response.isSuccessful
        } else {
            throw IllegalArgumentException(response.body().toString())
        }
    }

    suspend fun createConversation(userId: Long, body: CreateConversation): Boolean {
        val response = conversationApi.createConversation(userId, body)
        return response.isSuccessful
    }

    suspend fun getAllMessage(userId: Long, friendId: Long): List<Message>? {
        val response = conversationApi.getAllMessage(userId, friendId)
        return if (response.isSuccessful){
            response.body()
        } else {
            throw Exception("Lỗi lấy tin nhắn: ${response.code()}")
        }
    }

    suspend fun getAllConversation(userId: Long): List<GetConversation>? {
        val response = conversationApi.getAllConversation(userId)
        return if (response.isSuccessful){
            response.body()
        } else {
            throw Exception("Lỗi lấy hội thoại: ${response.code()}")
        }
    }

    suspend fun sendToken(fcmTokenResponse: FcmTokenResponse): Boolean {
        val response = fcmApi.sendTokenFcm(fcmTokenResponse)
        return if (response.isSuccessful) {
            true
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
            Log.e("FCM", "Send token failed: HTTP ${response.code()} - $errorMsg")
            false
        }
    }

}