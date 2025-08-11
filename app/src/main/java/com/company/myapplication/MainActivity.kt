package com.company.myapplication

import android.content.pm.PackageManager
import android.Manifest
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.company.myapplication.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(this)
        }
        askNotificationPermission()
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.e("Permission", "Đã cấp quyền POST_NOTIFICATIONS")
            } else {
                Log.e("Permission", "Người dùng từ chối quyền POST_NOTIFICATIONS")
                // Có thể cho hiện Snackbar/Tôiểu toast giải thích hoặc dẫn vào Settings
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                // Đã có quyền
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.e("Permission", "Quyền thông báo đã được cấp")
                }

                // Nên giải thích trước khi xin
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    AlertDialog.Builder(this)
                        .setTitle("Quyền thông báo")
                        .setMessage("Ứng dụng cần quyền thông báo để gửi tin nhắn đến bạn ngay khi có hoạt động mới.")
                        .setPositiveButton("Cho phép") { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton("Không") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

                // Xin trực tiếp
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

}
