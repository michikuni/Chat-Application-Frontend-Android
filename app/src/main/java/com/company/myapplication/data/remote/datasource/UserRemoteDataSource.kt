package com.company.myapplication.data.remote.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import com.company.myapplication.data.api.UserApi
import com.company.myapplication.data.model.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
) {
    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }

    suspend fun uploadImage(context: Context, uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = uriToFile(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = userApi.uploadAvatar(body)
            response.isSuccessful
        } catch (e: IOException) {
            Log.e("UserRemote", "uploadImage IO: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e("UserRemote", "uploadImage error: ${e.message}", e)
            false
        }
    }

    suspend fun getUserInfo(userId: Long): UserResponse? = try {
        val response = userApi.getUserInfo(userId)
        if (response.isSuccessful) response.body() else null
    } catch (e: Exception) {
        Log.e("UserRemote", "getUserInfo error: ${e.message}", e)
        null
    }
}
