package com.company.myapplication.data.api

import okhttp3.*
import retrofit2.http.*
import retrofit2.Response

interface UserApi {
    @Multipart
    @POST("api/users/upload_avatar/{userId}")
    suspend fun uploadAvatar(
        @Path("userId") userId: Long,
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
}