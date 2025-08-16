package com.company.myapplication.util

import android.content.Context
import androidx.core.content.edit

object DataChangeHelper {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_DATA_CHANGED = "data_changed"

    fun setDataChanged(context: Context, changed: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_DATA_CHANGED, changed) }
    }

    fun hasDataChanged(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DATA_CHANGED, false)
    }
}
