package com.company.myapplication.core.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastHelper {
    const val FEATURE_UNAVAILABLE = "Chức năng hiện tại không khả dụng"

    fun showFeatureUnavailable(context: Context) {
        showOnMain(context, FEATURE_UNAVAILABLE)
    }

    fun showOnMain(context: Context, message: String) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
