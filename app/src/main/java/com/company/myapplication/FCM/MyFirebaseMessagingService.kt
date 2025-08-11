package com.company.myapplication.FCM

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.company.myapplication.R
import com.company.myapplication.util.DataChangeHelper
import com.company.myapplication.util.UserSharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "Tin nhắn mới"
        val timestamp = remoteMessage.data["time"]?.toLong() ?: System.currentTimeMillis()
        val userId = remoteMessage.data["userId"]?.toLong() ?: -1
        val message = remoteMessage.data["message"] ?: ""
        val userIdUSP = UserSharedPreferences.getId(this)
        Log.e("Fiirebase mes", "ms: $userId USP: $userIdUSP")
        DataChangeHelper.setDataChanged(this, true)

        // TODO: Xử lý đồng bộ message vào local DB, update UI, v.v.
        if (userIdUSP == userId){
            remoteMessage.notification?.title?.let { Log.e("ms reicei", it) }
            remoteMessage.data["time"]?.let { Log.e("ms reicei time", it) }
            remoteMessage.data["message"]?.let { Log.e("ms reicei message", it) }

            showNotification(title, message, timestamp)
        }
    }
    private fun showNotification(title: String, body: String, time: Long) {
        val channelId = "chat_messages"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val timeString = formatTimeHumanReadable(time)

        // Tạo channel nếu chưa có (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId,
                    "Mir Communicate",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.chat_apps_script_24px)
            .setContentTitle(title)
            .setContentText("$body - $timeString")
            .setWhen(time)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MyFirebaseMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("FCM", "Quyền POST_NOTIFICATIONS chưa được cấp, không thể hiện thông báo!")
                return@with
            }

            val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            notify(notificationId, notificationBuilder.build())
            Log.e("FCM", "Đã hiện thông báo ID: $notificationId")
        }
    }

    override fun onNewToken(token: String) {
        UserSharedPreferences.saveFcmToken(this, token)
        Log.e("FCM", "New token: $token")
    }

    private fun formatTimeHumanReadable(timeMillis: Long): String {
        val cal = Calendar.getInstance()
        val now = Calendar.getInstance()

        cal.timeInMillis = timeMillis

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val hourText = "$hour" + if (minute != 0) ":$minute" else ""
        val amPmText = if (hour < 12) "sáng" else "chiều"

        // Nếu cùng ngày hôm nay
        return if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
            "$hourText $amPmText hôm nay"
        }
        // Nếu hôm qua
        else if (now.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR) == 1 &&
            cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            "$hourText $amPmText hôm qua"
        }
        // Ngày khác
        else {
            val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(timeMillis))
        }
    }


}