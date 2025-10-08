package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
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
    val isCurrentUser = userId == message.senderId.id
    val avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${message.senderId.id}")}
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // Nếu KHÔNG phải tin nhắn của current user => thêm avatar
        if (!isCurrentUser) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = avatarUrl,
                    error = painterResource(R.drawable.person),
                    fallback = painterResource(R.drawable.person)
                ),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .align (Alignment.CenterVertically)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Column {
            if (!isCurrentUser) {
                Text(
                    text = message.senderId.name, fontFamily = titleFont,
                    fontSize = 10.sp, color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
            // Bong bóng chat
            Box(
                modifier = Modifier
                    .widthIn(max = screenWidth * 0.6f)
                    .background(
                        color = if (isCurrentUser)
                            Color(color[3].removePrefix("0x").toLong(16))
                        else
                            Color(color[4].removePrefix("0x").toLong(16)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp)
            ) {

                when {
                    message.content != null -> {
                        Text(
                            text = message.content,
                            color = if (isCurrentUser)
                                Color(color[4].removePrefix("0x").toLong(16))
                            else
                                Color(color[5].removePrefix("0x").toLong(16)),
                            fontFamily = titleFont,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    message.mediaFile != null -> {
                        AsyncImage(
                            model = image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .sizeIn(maxWidth = screenWidth * 0.5f, maxHeight = 250.dp)
                        )
                    }
                }
            }
        }
        // Nếu là tin nhắn của current user thì chừa khoảng trống cho đẹp
//        if (isCurrentUser) {
//            Spacer(modifier = Modifier.width(40.dp))
//        }
    }
}
