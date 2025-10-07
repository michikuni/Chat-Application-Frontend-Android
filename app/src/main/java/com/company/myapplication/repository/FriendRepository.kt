package com.company.myapplication.repository

import android.content.Context
import android.util.Log
import com.company.myapplication.data.api.FriendApi
import com.company.myapplication.data.model.friend.FriendRequestDTO
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class FriendRepository(context: Context) {

    private fun createRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
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

    // --- 1. Lấy danh sách bạn bè ---
    suspend fun getAllFriendsById(userId: Long): List<UserResponse> {
        return try {
            val response = friendApi.getAllFriendsById(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("FriendRepo", "❌ Lỗi lấy danh sách bạn bè: ${response.code()} | ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi getAllFriendsById: ${e.message}", e)
            emptyList()
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định khi getAllFriendsById: ${e.message}", e)
            emptyList()
        }
    }

    // --- 2. Tìm bạn qua email ---
    suspend fun getFriendByEmail(email: String): UserResponse? {
        return try {
            val response = friendApi.getFriendByEmail(email)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("FriendRepo", "❌ Lỗi getFriendByEmail: ${response.code()} | ${response.errorBody()?.string()}")
                null
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi getFriendByEmail: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định getFriendByEmail: ${e.message}", e)
            null
        }
    }

    // --- 3. Gửi lời mời kết bạn ---
    suspend fun sendAddRequest(userId: Long, receiverEmail: String): String? {
        return try {
            val request = FriendRequestDTO(receiverEmail = receiverEmail)
            val response = friendApi.sendAddRequest(userId, request)

            if (response.isSuccessful) {
                val body = response.body()
                body?.message ?: body?.error ?: "Thành công"
            } else {
                val error = response.errorBody()?.string()
                Log.e("FriendRepo", "❌ Lỗi sendAddRequest: ${response.code()} | $error")
                error ?: "Lỗi không xác định"
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi gửi yêu cầu kết bạn: ${e.message}", e)
            "Không thể kết nối tới server"
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định sendAddRequest: ${e.message}", e)
            "Lỗi không xác định"
        }
    }

    // --- 4. Danh sách yêu cầu đang chờ ---
    suspend fun getPendingFriends(userId: Long): List<FriendResponse> {
        return try {
            val response = friendApi.getPendingFriends(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("FriendRepo", "❌ Lỗi lấy danh sách pending: ${response.code()} | ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi getPendingFriends: ${e.message}", e)
            emptyList()
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định getPendingFriends: ${e.message}", e)
            emptyList()
        }
    }

    // --- 5. Danh sách yêu cầu đã nhận ---
    suspend fun getRequestFriends(userId: Long): List<FriendResponse> {
        return try {
            val response = friendApi.getRequestFriends(userId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("FriendRepo", "❌ Lỗi lấy danh sách request: ${response.code()} | ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi getRequestFriends: ${e.message}", e)
            emptyList()
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định getRequestFriends: ${e.message}", e)
            emptyList()
        }
    }

    // --- 6. Chấp nhận lời mời ---
    suspend fun acceptedFriendRequest(friendshipId: Long): Boolean {
        return try {
            val response = friendApi.acceptedFriendRequest(friendshipId)
            if (response.isSuccessful) true
            else {
                Log.e("FriendRepo", "❌ Lỗi acceptedFriendRequest: ${response.code()} | ${response.errorBody()?.string()}")
                false
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi acceptedFriendRequest: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định acceptedFriendRequest: ${e.message}", e)
            false
        }
    }

    // --- 7. Hủy yêu cầu kết bạn ---
    suspend fun cancelFriendRequest(friendshipId: Long): Boolean {
        return try {
            val response = friendApi.cancelFriendRequest(friendshipId)
            if (response.isSuccessful) true
            else {
                Log.e("FriendRepo", "❌ Lỗi cancelFriendRequest: ${response.code()} | ${response.errorBody()?.string()}")
                false
            }
        } catch (e: IOException) {
            Log.e("FriendRepo", "Lỗi mạng khi cancelFriendRequest: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e("FriendRepo", "Lỗi không xác định cancelFriendRequest: ${e.message}", e)
            false
        }
    }
}
