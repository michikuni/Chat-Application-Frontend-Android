package com.company.myapplication.ui.home.moment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.data.model.feed.FeedDTO
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig

@Composable
fun PostCard(
    userInfo: UserResponse?,
    feedInfo: FeedDTO
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val media by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/feed/getMediaFile/${feedInfo.mediaFile}") }
        Column {
            // Phần Header: Avatar, Tên người dùng, Thời gian
            PostHeader(
                userName = userInfo?.name ?: "Unknown",
                postTime = feedInfo.createdAt,
                userId = userInfo?.id ?: -1L
            )

            // Phần Caption
            if (!feedInfo.content.isNullOrBlank()) {
                Text(
                    text = feedInfo.content,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 16.sp
                )
            }

            // Phần Ảnh
            if (!feedInfo.mediaFile.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter("${ApiConfig.BASE_URL}/api/feed/getMediaFile/${feedInfo.mediaFile}"),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
            // Phần thống kê Like, Comment
            EngagementStats(likeCount = "", commentCount = "", likeIconResId = R.drawable.like)

            // Dấu gạch ngang
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Phần nút chức năng: Like, Comment
            ActionButtons()
        }
    }
}

@Composable
fun PostHeader(
    userName: String,
    postTime: String,
    userId: Long
) {
    val avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${userId}") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = avatarUrl,
                error = painterResource(R.drawable.person),
                fallback = painterResource(R.drawable.person)
            ),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = postTime, color = Color.Gray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /* TODO: Handle more options click */ }) {
            Icon(Icons.Default.MoreHoriz, contentDescription = "More Options")
        }
    }
}

@Composable
fun EngagementStats(likeCount: String, commentCount: String, likeIconResId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = likeIconResId),
            contentDescription = "Like Icon",
            modifier = Modifier.size(20.dp)
        )
//        Text(
//            text = likeCount,
//            color = Color.Gray,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(start = 4.dp)
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        Text(text = commentCount, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun ActionButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ActionButton(
            icon = { Icon(Icons.Outlined.ThumbUp, contentDescription = "Like") },
            text = "Thích"
        ) { /* TODO: Handle Like click */ }

        ActionButton(
            icon = { Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment") },
            text = "Bình luận"
        ) { /* TODO: Handle Comment click */ }
    }
}

@Composable
fun ActionButton(
    icon: @Composable () -> Unit,
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, color = Color.Gray, fontWeight = FontWeight.SemiBold)
        }
    }
}