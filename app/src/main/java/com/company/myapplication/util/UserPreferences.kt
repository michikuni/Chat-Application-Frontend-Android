package com.company.myapplication.util

import android.content.Context
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
}