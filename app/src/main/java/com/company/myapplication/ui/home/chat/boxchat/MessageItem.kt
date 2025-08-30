package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.company.myapplication.data.model.chat.Message
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.titleFont


@Composable
fun MessageItem(
    message: Message,
    userId: Long,
    color: List<String>
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val image = "${ApiConfig.BASE_URL}/api/chats/getMediaFile/${message.mediaFile}"
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
                    if (isCurrentUser) Color(color[3].removePrefix("0x").toLong(16)) else
                        Color.LightGray,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            if (message.content != null){
                Text(
                    text = message.content,
                    color = if (isCurrentUser)
                        Color(color[5].removePrefix("0x").toLong(16))
                    else
                        Color(color[4].removePrefix("0x").toLong(16)),
                    fontFamily = titleFont,
                    modifier = Modifier
                        .padding(12.dp)
                )
            } else if (message.mediaFile != null) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .sizeIn(maxWidth = screenWidth * 0.6f, maxHeight = 250.dp) // giới hạn ảnh
                )
            }
        }
    }
}
