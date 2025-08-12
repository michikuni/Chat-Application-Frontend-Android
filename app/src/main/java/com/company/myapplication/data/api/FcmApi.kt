package com.company.myapplication.data.api

import com.company.myapplication.data.model.fcm.FcmTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/api/fcm/save-token")
    suspend fun sendTokenFcm(
        @Body sendToken: FcmTokenResponse
    ): Response<Void>
}