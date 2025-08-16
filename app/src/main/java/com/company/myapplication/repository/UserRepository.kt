package com.company.myapplication.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.company.myapplication.data.api.FriendApi
import com.company.myapplication.data.api.UserApi
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class UserRepository (context: Context){
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
    private val userApi = retrofit.create(UserApi::class.java)

    suspend fun uploadImage(context: Context, uri: Uri, userId: Long) {
        val contentResolver = context.contentResolver

        // Mở input stream từ Uri
        val inputStream = contentResolver.openInputStream(uri) ?: return
        val tempFile = File(context.cacheDir, "temp_image.jpg")
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        // Tạo RequestBody từ file
        val requestFile = tempFile
            .asRequestBody("image/*".toMediaTypeOrNull())

        val body = MultipartBody.Part.createFormData(
            "file", tempFile.name, requestFile
        )

        val response = userApi.uploadAvatar(userId, body)
        if (response.isSuccessful) {
            Log.d("UPLOAD", "Thành công: ${response.body()?.string()}")
        } else {
            Log.e("UPLOAD", "Lỗi: ${response.errorBody()?.string()}")
        }
    }

}