package com.company.myapplication.util

import android.content.Context

object UserSharedPreferences{
    private const val PREF_NAME = "auth_session"
    private const val KEY_ID = "id"
    private const val KEY_USERNAME = "username"
    private const val KEY_TOKEN = "token"

    fun saveUser(context: Context, id: Long, username: String, token: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putLong(KEY_ID, id)
            putString(KEY_USERNAME, username)
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
    }

    fun getId(context: Context): Long {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getLong(KEY_ID, -1)
    }

    fun clearSession(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}