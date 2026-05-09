package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.response.AuthResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthFirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private companion object { const val TAG = "AuthFirestore" }

    suspend fun login(account: String, password: String): LoginResponse? {
        return try {
            val snapshot = firestore.collection(FirestoreCollections.USERS)
                .whereEqualTo("username", account)
                .limit(1)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
                ?: throw IllegalArgumentException("Tài khoản không tồn tại")
            val storedPassword = doc.getString("password")
            if (storedPassword != password) throw IllegalArgumentException("Mật khẩu không đúng")

            val id = doc.getLong("id") ?: 0L
            val username = doc.getString("username") ?: account
            // Firestore không cấp JWT thực - dùng docId làm token giả lập
            LoginResponse(id = id, username = username, token = "firestore:${doc.id}")
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message}", e)
            throw IllegalArgumentException("Đăng nhập thất bại")
        }
    }

    suspend fun register(name: String, account: String, email: String, password: String): Boolean {
        return try {
            val users = firestore.collection(FirestoreCollections.USERS)
            val existed = users.whereEqualTo("username", account).limit(1).get().await()
            if (!existed.isEmpty) throw IllegalArgumentException("Tài khoản đã tồn tại")

            val id = System.currentTimeMillis()
            val data = mapOf(
                "id" to id,
                "name" to name,
                "username" to account,
                "email" to email,
                "password" to password,
                "avatar" to null
            )
            users.document(id.toString()).set(data).await()
            true
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "register: ${e.message}", e)
            throw IllegalArgumentException("Đăng ký thất bại")
        }
    }

    suspend fun checkTokenValid(token: String?): AuthResponse? {
        if (token.isNullOrEmpty() || !token.startsWith("firestore:")) {
            return AuthResponse(valid = false)
        }
        return try {
            val docId = token.removePrefix("firestore:")
            val doc = firestore.collection(FirestoreCollections.USERS).document(docId).get().await()
            if (doc.exists()) {
                AuthResponse(valid = true, username = doc.getString("username"))
            } else AuthResponse(valid = false)
        } catch (e: Exception) {
            Log.e(TAG, "checkTokenValid: ${e.message}", e)
            null
        }
    }
}
