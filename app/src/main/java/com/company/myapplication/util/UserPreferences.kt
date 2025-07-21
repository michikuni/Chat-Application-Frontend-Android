package com.company.myapplication.util

import android.app.Activity
import android.content.Context
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.company.myapplication.data.model.auth.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")
    private val dataStore = context.dataStore

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }

    fun saveLogin(context: Activity, dto: LoginResponse) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit()
            .putLong("id", dto.id)
            .putString("token", dto.token)
            .putString("username", dto.username)
            .apply()
    }

}