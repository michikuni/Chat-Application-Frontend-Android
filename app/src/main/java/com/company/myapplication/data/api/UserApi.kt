package com.company.myapplication.data.api

import com.company.myapplication.data.model.response.UserResponse
import okhttp3.*
import retrofit2.http.*
import retrofit2.Response

interface UserApi {
    @Multipart
    @POST("api/users/upload_avatar")
    suspend fun uploadAvatar(
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>

    @GET("api/users/get_user_info/{userId}")
    suspend fun getUserInfo(
        @Path("userId") userId: Long
    ): Response<UserResponse>
}