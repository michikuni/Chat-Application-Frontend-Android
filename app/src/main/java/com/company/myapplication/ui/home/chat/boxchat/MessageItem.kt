package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.viewmodel.AuthViewModel


@Composable
fun MessageItem(
    message: Message,
    userId: Long
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        val isCurrentUser = userId == message.senderId.id
        Row(
            modifier = Modifier
                .widthIn(max = screenWidth * 0.5f)
                .align(
                    (if (isCurrentUser) Alignment.CenterEnd else
                        Alignment.CenterStart))

                .background(
                    color =
                    if (isCurrentUser) Color.Blue else
                        Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (isCurrentUser) Color.White else Color.Black
            )
        }
    }
}
