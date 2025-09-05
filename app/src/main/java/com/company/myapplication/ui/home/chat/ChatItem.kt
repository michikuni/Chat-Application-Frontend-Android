package com.company.myapplication.ui.home.chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.data.model.chat.ConversationDTO
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.util.convertTimestamp
import com.company.myapplication.util.CustomMapper.filterUser
import com.company.myapplication.util.CustomMapper.toViewDTO
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.titleFont

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChatItem(
    conversation: ConversationDTO,
    navHostController: NavHostController,
    context: Context,
    userInfo: UserResponse
) {
    var friendId by remember { mutableLongStateOf(-1) }
    val userId = UserSharedPreferences.getId(context = context)
    val toViewConversation = conversation.toViewDTO()
    val conversationViewFilter = toViewConversation.filterUser(user = userInfo)

    if (conversationViewFilter.conversationType == "PAIR"){
        friendId = conversationViewFilter.membersIds[0]
    }
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${friendId}") }
    var avatarGroupUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/chats/getConversationAvatar/${conversationViewFilter.groupAvatar}") }
    var isSelected by remember { mutableStateOf(false) }
    val time = convertTimestamp(conversationViewFilter.createdAt.toString())
    Row(
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFFEEEEEE) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                isSelected = !isSelected
                navHostController.navigate(
                    route = "box_chat/${conversationViewFilter.id}/${conversationViewFilter.name[0]}"
                )
            }
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (conversationViewFilter.conversationType == "PAIR"){
            Box{
                Image(
                    painter = rememberAsyncImagePainter(
                        model = avatarUrl,
                        error = painterResource(R.drawable.person),
                        fallback = painterResource(R.drawable.person)
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(color = backgroundColor)
                )
                if (conversationViewFilter.isRead != null) {
                    Box(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .background(Color.Green, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(text = conversationViewFilter.name[0], fontWeight = FontWeight.Bold, fontFamily = titleFont)
                if (userId == conversationViewFilter.senderId){
                    if (conversationViewFilter.content != null){
                        Text(
                            text = "Bạn: ${conversationViewFilter.content}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    } else {
                        Text(
                            text = "Bạn đã gửi một ảnh",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    }
                } else {
                    if (conversationViewFilter.content != null){
                        Text(
                            text = conversationViewFilter.content,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    } else {
                        Text(
                            text = "Đã gửi một ảnh",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    }
                }

            }
            Text(text = time, fontSize = 12.sp, color = Color.Gray, fontFamily = titleFont)
        } else if(conversationViewFilter.conversationType == "GROUP"){
            Box{
                Image(
                    painter = rememberAsyncImagePainter(
                        model = avatarGroupUrl,
                        error = painterResource(R.drawable.person),
                        fallback = painterResource(R.drawable.person)
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(color = backgroundColor)
                )
                if (conversationViewFilter.groupAvatar != null) {
                    Box(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .background(Color.Green, CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                if (conversationViewFilter.conversationName.isNullOrBlank()) {
                    Text(
                        text = "Nhóm chưa có tên",
                        fontWeight = FontWeight.Bold,
                        fontFamily = titleFont
                    )
                } else {
                    Text(
                        text = conversationViewFilter.conversationName,
                        fontWeight = FontWeight.Bold,
                        fontFamily = titleFont
                    )
                }

                if (userId == conversationViewFilter.senderId){
                    if (!conversationViewFilter.content.isNullOrBlank() && conversationViewFilter.mediaFile.isNullOrBlank()){
                        Text(
                            text = "Bạn: ${conversationViewFilter.content}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    } else if(!conversationViewFilter.mediaFile.isNullOrBlank() && conversationViewFilter.content.isNullOrBlank()) {
                        Text(
                            text = "Bạn đã gửi một ảnh",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    } else if (conversationViewFilter.content.isNullOrBlank() && conversationViewFilter.mediaFile.isNullOrBlank()){
                        Text(
                            text = "Nhóm đã được tạo",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    }
                } else {
                    if (!conversationViewFilter.content.isNullOrBlank() && conversationViewFilter.mediaFile.isNullOrBlank()){
                        Text(
                            text = conversationViewFilter.content,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    } else if (conversationViewFilter.content.isNullOrBlank() && !conversationViewFilter.mediaFile.isNullOrBlank()){
                        Text(
                            text = "Đã gửi một ảnh",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    } else if (conversationViewFilter.content.isNullOrBlank() && conversationViewFilter.mediaFile.isNullOrBlank()){
                        Text(
                            text = "Nhóm đã được tạo",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = titleFont
                        )
                    }
                }

            }
            Text(text = time, fontSize = 12.sp, color = Color.Gray, fontFamily = titleFont)
        }
    }
}
