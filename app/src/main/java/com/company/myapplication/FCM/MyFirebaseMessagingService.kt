package com.company.myapplication.FCM

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToServer(token)
    }
    private fun sendTokenToServer(token: String) {
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data["type"]?.let { type ->
            if (type == "SYNC") {
                // Gọi API backend để lấy dữ liệu mới
            }
        }
    }

}