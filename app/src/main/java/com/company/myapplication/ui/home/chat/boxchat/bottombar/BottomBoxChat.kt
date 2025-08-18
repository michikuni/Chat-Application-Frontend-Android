package com.company.myapplication.ui.home.chat.boxchat.bottombar

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.util.themeColor
import com.company.myapplication.ui.home.util.TextField
import com.company.myapplication.util.DataChangeHelper
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.ConversationViewModel


@Composable
fun BottomBoxChat(
    conversationViewModel: ConversationViewModel,
    userId: Long,
    friendId: Long,
    activity: Activity
){
    var chatMessage by remember(userId, friendId) { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {},
                modifier = Modifier.weight(0.125f)) {
                Icon(
                    imageVector = Icons.Default.Attachment,
                    contentDescription = "Attach",
                    tint = themeColor
                )
            }

            TextField(query = chatMessage,
                onQueryChange = { chatMessage = it},
                text = "Nhập tin nhắn ...",
                multiLine = true,
                color = topAppBarColor,
                modifier = Modifier.weight(0.75f)
            )

            IconButton(
                onClick = {
                    Log.e(">>> ChatMessage:", chatMessage)
                    if (chatMessage.isNotBlank()) {
                        Log.e(">>> ChatMessage111ooooo:", chatMessage)
                        conversationViewModel.createConversation(
                            userId = userId,
                            body = CreateConversation(friendId = friendId, message = chatMessage)
                        )
                    }
                    chatMessage = ""
                    DataChangeHelper.setDataChanged(activity, true)
                },
                modifier = Modifier.weight(0.125f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = themeColor
                )
            }
        }
        Spacer(modifier = Modifier.width(30.dp))
    }
}