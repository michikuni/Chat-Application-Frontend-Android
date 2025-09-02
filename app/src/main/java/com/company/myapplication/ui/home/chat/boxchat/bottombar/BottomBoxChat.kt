package com.company.myapplication.ui.home.chat.boxchat.bottombar

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.company.myapplication.R
import com.company.myapplication.data.model.chat.CreateConversation
import com.company.myapplication.repository.ConversationRepository
import com.company.myapplication.ui.home.util.TextField
import com.company.myapplication.util.DataChangeHelper
import com.company.myapplication.viewmodel.ConversationViewModel
import kotlinx.coroutines.launch


@Composable
fun BottomBoxChat(
    conversationViewModel: ConversationViewModel,
    userId: Long,
    friendId: Long,
    activity: Activity,
    color: List<String>
){
    var chatMessage by remember(userId, friendId) { mutableStateOf("") }
    val repo = ConversationRepository(context = activity)
    val scope = rememberCoroutineScope()
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                repo.sendMediaFile(
                    context = activity,
                    userId = userId,
                    friendId = friendId,
                    uri = it
                )
            }
        }
    }

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
            IconButton(onClick = {
                imageLauncher.launch("image/*")
            },
                modifier = Modifier.weight(0.125f)) {
                Icon(
                    painter = painterResource(id = R.drawable.paperclip),
                    contentDescription = "Attach",
                    tint = Color(color[3].removePrefix("0x").toLong(16))
                )
            }

            TextField(query = chatMessage,
                onQueryChange = { chatMessage = it},
                text = "Nhập tin nhắn ...",
                multiLine = true,
                color = Color(color[3].removePrefix("0x").toLong(16)),
                modifier = Modifier.weight(0.75f)
            )

            IconButton(
                onClick = {
                    if (chatMessage.isNotBlank()) {
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
                    painter = painterResource(id = R.drawable.arrow_up_circle_fill),
                    contentDescription = "Send",
                    tint = Color(color[3].removePrefix("0x").toLong(16))
                )
            }
        }
        Spacer(modifier = Modifier.width(30.dp))
    }
}