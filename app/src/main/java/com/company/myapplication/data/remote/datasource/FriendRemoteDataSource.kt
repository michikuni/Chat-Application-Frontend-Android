package com.company.myapplication.data.remote.datasource

import android.util.Log
import com.company.myapplication.data.api.FriendApi
import com.company.myapplication.data.model.friend.FriendRequestDTO
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRemoteDataSource @Inject constructor(
    private val friendApi: FriendApi
) {
    private companion object { const val TAG = "FriendRemote" }

    suspend fun getAllFriendsById(userId: Long): List<UserResponse> = try {
        val response = friendApi.getAllFriendsById(userId)
        if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    } catch (e: Exception) {
        Log.e(TAG, "getAllFriendsById: ${e.message}", e)
        emptyList()
    }

    suspend fun getFriendByEmail(email: String): UserResponse? = try {
        val response = friendApi.getFriendByEmail(email)
        if (response.isSuccessful) response.body() else null
    } catch (e: Exception) {
        Log.e(TAG, "getFriendByEmail: ${e.message}", e)
        null
    }

    suspend fun sendAddRequest(userId: Long, receiverEmail: String): String? = try {
        val request = FriendRequestDTO(receiverEmail = receiverEmail)
        val response = friendApi.sendAddRequest(userId, request)
        if (response.isSuccessful) {
            val body = response.body()
            body?.message ?: body?.error ?: "Thành công"
        } else {
            response.errorBody()?.string() ?: "Lỗi không xác định"
        }
    } catch (e: IOException) {
        "Không thể kết nối tới server"
    } catch (e: Exception) {
        "Lỗi không xác định"
    }

    suspend fun getPendingFriends(userId: Long): List<FriendResponse> = try {
        val response = friendApi.getPendingFriends(userId)
        if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    } catch (e: Exception) {
        Log.e(TAG, "getPendingFriends: ${e.message}", e)
        emptyList()
    }

    suspend fun getRequestFriends(userId: Long): List<FriendResponse> = try {
        val response = friendApi.getRequestFriends(userId)
        if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    } catch (e: Exception) {
        Log.e(TAG, "getRequestFriends: ${e.message}", e)
        emptyList()
    }

    suspend fun acceptedFriendRequest(friendshipId: Long): Boolean = try {
        friendApi.acceptedFriendRequest(friendshipId).isSuccessful
    } catch (e: Exception) {
        Log.e(TAG, "acceptedFriendRequest: ${e.message}", e)
        false
    }

    suspend fun cancelFriendRequest(friendshipId: Long): Boolean = try {
        friendApi.cancelFriendRequest(friendshipId).isSuccessful
    } catch (e: Exception) {
        Log.e(TAG, "cancelFriendRequest: ${e.message}", e)
        false
    }
}
