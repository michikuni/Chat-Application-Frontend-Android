package com.company.myapplication.data.firestore.datasource

import android.util.Log
import com.company.myapplication.data.firestore.FirestoreCollections
import com.company.myapplication.data.model.auth.LoginResponse
import com.company.myapplication.data.model.response.AuthResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthFirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private companion object { const val TAG = "AuthFirebaseAuth" }

    suspend fun login(account: String, password: String): LoginResponse? {
        return try {
            // Resolve email: nếu account không chứa '@', tra cứu email từ Firestore
            val email = resolveEmail(account)
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalArgumentException("Đăng nhập thất bại")
            val idToken = user.getIdToken(false).await().token
                ?: throw IllegalArgumentException("Không lấy được token")

            val docSnapshot = firestore.collection(FirestoreCollections.USERS)
                .document(user.uid)
                .get()
                .await()

            val id = docSnapshot.getLong("id") ?: user.uid.hashCode().toLong()
            val username = docSnapshot.getString("username") ?: account

            LoginResponse(id = id, username = username, token = idToken)
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message}", e)
            val message = mapFirebaseAuthError(e.message)
            throw IllegalArgumentException(message)
        }
    }

    suspend fun register(name: String, account: String, email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalArgumentException("Đăng ký thất bại")

            val id = System.currentTimeMillis()
            val data = mapOf(
                "id" to id,
                "name" to name,
                "username" to account,
                "email" to email,
                "avatar" to null
            )
            firestore.collection(FirestoreCollections.USERS)
                .document(user.uid)
                .set(data)
                .await()
            true
        } catch (e: IllegalArgumentException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "register: ${e.message}", e)
            val message = mapFirebaseAuthError(e.message)
            throw IllegalArgumentException(message)
        }
    }

    suspend fun checkTokenValid(token: String?): AuthResponse? {
        if (token.isNullOrEmpty()) return AuthResponse(valid = false)
        return try {
            val user = firebaseAuth.currentUser ?: return AuthResponse(valid = false)
            // Force refresh để xác nhận token còn hiệu lực với Firebase
            val idToken = user.getIdToken(true).await().token
            if (idToken.isNullOrEmpty()) return AuthResponse(valid = false)

            val docSnapshot = firestore.collection(FirestoreCollections.USERS)
                .document(user.uid)
                .get()
                .await()

            if (docSnapshot.exists()) {
                AuthResponse(valid = true, username = docSnapshot.getString("username"))
            } else {
                AuthResponse(valid = false)
            }
        } catch (e: Exception) {
            Log.e(TAG, "checkTokenValid: ${e.message}", e)
            AuthResponse(valid = false)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    private suspend fun resolveEmail(account: String): String {
        if (account.contains("@")) return account
        val snapshot = firestore.collection(FirestoreCollections.USERS)
            .whereEqualTo("username", account)
            .limit(1)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.getString("email")
            ?: throw IllegalArgumentException("Tài khoản không tồn tại")
    }

    private fun mapFirebaseAuthError(message: String?): String = when {
        message == null -> "Lỗi xác thực"
        message.contains("email address is already in use") -> "Email đã được sử dụng"
        message.contains("badly formatted") -> "Email không hợp lệ"
        message.contains("password is invalid") || message.contains("no user record") -> "Tài khoản hoặc mật khẩu không đúng"
        message.contains("user may have been deleted") -> "Tài khoản không tồn tại"
        message.contains("too many requests") -> "Quá nhiều lần thử, vui lòng thử lại sau"
        message.contains("network error") -> "Lỗi kết nối mạng"
        else -> "Lỗi xác thực: $message"
    }
}
