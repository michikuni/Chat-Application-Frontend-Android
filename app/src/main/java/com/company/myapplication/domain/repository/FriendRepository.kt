package com.company.myapplication.domain.repository

import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse

interface FriendRepository {
    suspend fun getAllFriendsById(userId: Long): List<UserResponse>
    suspend fun getFriendByEmail(email: String): UserResponse?
    suspend fun sendAddRequest(userId: Long, receiverEmail: String): String?
    suspend fun getPendingFriends(userId: Long): List<FriendResponse>
    suspend fun getRequestFriends(userId: Long): List<FriendResponse>
    suspend fun acceptedFriendRequest(friendshipId: Long): Boolean
    suspend fun cancelFriendRequest(friendshipId: Long): Boolean
}
