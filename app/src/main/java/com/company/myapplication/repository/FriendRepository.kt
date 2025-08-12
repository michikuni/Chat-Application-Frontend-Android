package com.company.myapplication.repository

import android.content.Context
import com.company.myapplication.data.api.FriendApi
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FriendRepository (context: Context){
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
    private val friendApi = retrofit.create(FriendApi::class.java)

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
}