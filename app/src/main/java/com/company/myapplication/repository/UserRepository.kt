package com.company.myapplication.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.company.myapplication.data.api.UserApi
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.repository.interceptor.AuthInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class UserRepository(context: Context) {

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
    private val userApi = retrofit.create(UserApi::class.java)

    // --- Chuyển Uri sang File ---
    fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    // --- Upload ảnh đại diện ---
    suspend fun uploadImage(context: Context, uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = uriToFile(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = userApi.uploadAvatar(body)

            val bodyString = try {
                response.body()?.string()
            } catch (e: Exception) {
                null // tránh EOFException
            }

            val errorString = try {
                response.errorBody()?.string()
            } catch (e: Exception) {
                null
            }

            return@withContext if (response.isSuccessful) {
                Log.e("✅ Upload thành công:", "${bodyString ?: "Không có nội dung"}")
                true
            } else {
                Log.e("❌ Upload thất bại:", "Code: ${response.code()} | Lỗi: $errorString")
                false
            }
        } catch (e: IOException) {
            Log.e("UserRepo", "Lỗi mạng khi uploadImage: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e("UserRepo", "Lỗi không xác định khi uploadImage: ${e.message}", e)
            false
        }
    }

    // --- Lấy thông tin user ---
    suspend fun getUserInfo(userId: Long): UserResponse? {
        return try {
            val response = userApi.getUserInfo(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                val err = response.errorBody()?.string()
                Log.e("UserRepo", "❌ Lỗi getUserInfo: ${response.code()} | $err")
                null
            }
        } catch (e: IOException) {
            Log.e("UserRepo", "Lỗi mạng khi getUserInfo: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("UserRepo", "Lỗi không xác định khi getUserInfo: ${e.message}", e)
            null
        }
    }
}
