package com.company.myapplication.domain.repository

import com.company.myapplication.data.model.fcm.FcmTokenResponse

interface FcmRepository {
    suspend fun sendToken(fcmTokenResponse: FcmTokenResponse): Boolean
}
