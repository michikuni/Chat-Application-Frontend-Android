package com.company.myapplication.data.repository

import com.company.myapplication.core.config.BackendSelector
import com.company.myapplication.core.config.BackendType
import com.company.myapplication.data.firestore.datasource.FcmFirestoreDataSource
import com.company.myapplication.data.model.fcm.FcmTokenResponse
import com.company.myapplication.data.remote.datasource.FcmRemoteDataSource
import com.company.myapplication.domain.repository.FcmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmRepositoryImpl @Inject constructor(
    private val backendSelector: BackendSelector,
    private val remote: FcmRemoteDataSource,
    private val firestore: FcmFirestoreDataSource
) : FcmRepository {
    override suspend fun sendToken(fcmTokenResponse: FcmTokenResponse): Boolean =
        when (backendSelector.current()) {
            BackendType.REMOTE -> remote.sendToken(fcmTokenResponse)
            BackendType.FIRESTORE -> firestore.sendToken(fcmTokenResponse)
        }
}
