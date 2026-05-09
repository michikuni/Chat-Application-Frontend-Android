package com.company.myapplication.data.repository

import com.company.myapplication.core.config.BackendSelector
import com.company.myapplication.core.config.BackendType
import com.company.myapplication.data.firestore.datasource.FriendFirestoreDataSource
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.data.remote.datasource.FriendRemoteDataSource
import com.company.myapplication.domain.repository.FriendRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepositoryImpl @Inject constructor(
    private val backendSelector: BackendSelector,
    private val remote: FriendRemoteDataSource,
    private val firestore: FriendFirestoreDataSource
) : FriendRepository {
    override suspend fun getAllFriendsById(userId: Long): List<UserResponse> =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getAllFriendsById(userId)
            BackendType.FIRESTORE -> firestore.getAllFriendsById(userId)
        }

    override suspend fun getFriendByEmail(email: String): UserResponse? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getFriendByEmail(email)
            BackendType.FIRESTORE -> firestore.getFriendByEmail(email)
        }

    override suspend fun sendAddRequest(userId: Long, receiverEmail: String): String? =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.sendAddRequest(userId, receiverEmail)
            BackendType.FIRESTORE -> firestore.sendAddRequest(userId, receiverEmail)
        }

    override suspend fun getPendingFriends(userId: Long): List<FriendResponse> =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getPendingFriends(userId)
            BackendType.FIRESTORE -> firestore.getPendingFriends(userId)
        }

    override suspend fun getRequestFriends(userId: Long): List<FriendResponse> =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.getRequestFriends(userId)
            BackendType.FIRESTORE -> firestore.getRequestFriends(userId)
        }

    override suspend fun acceptedFriendRequest(friendshipId: Long): Boolean =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.acceptedFriendRequest(friendshipId)
            BackendType.FIRESTORE -> firestore.acceptedFriendRequest(friendshipId)
        }

    override suspend fun cancelFriendRequest(friendshipId: Long): Boolean =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.cancelFriendRequest(friendshipId)
            BackendType.FIRESTORE -> firestore.cancelFriendRequest(friendshipId)
        }
}
