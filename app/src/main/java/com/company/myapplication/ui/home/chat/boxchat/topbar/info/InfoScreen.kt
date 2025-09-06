package com.company.myapplication.ui.home.chat.boxchat.topbar.info

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.company.myapplication.R
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen.ThemePopup
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.topbar.InfoTopBar
import com.company.myapplication.ui.home.setting.FeatureButton
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.ConversationViewModel
import com.company.myapplication.viewmodel.UserViewModel

@Composable
fun InfoScreen(
    userViewModel: UserViewModel,
    navHostController: NavHostController,
    userId: Long,
    conversationId: Long,
    conversationViewModel: ConversationViewModel,
    context: Activity
){
    Log.e("Info SC", conversationId.toString())
    LaunchedEffect(Unit) {
        conversationViewModel.getAllConversation(userId)
    }
    val conversation by conversationViewModel.conversation.collectAsState()
    var friendId by remember { mutableLongStateOf(-1) }

    val matchedConversation = conversation.find { it.id == conversationId }
    val listMembers: List<Long> = matchedConversation?.membersIds?.split(",")?.mapNotNull { it.trim().toLongOrNull() } ?: emptyList()
    if (matchedConversation != null){
        if (matchedConversation.conversationType == "PAIR"){
            listMembers.map {
                if (it != userId){
                    friendId = it
                }
            }
        }
    }

    Log.e("INFO FRIEND", conversation.toString())
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo(friendId)
    }
    val userInfo by userViewModel.userInfo.collectAsState()
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/$friendId") }
    var avatarGroupUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/chats/getConversationAvatar/${matchedConversation?.groupAvatar}") }
    var themePopup by remember { mutableStateOf(false) }
    Scaffold (
        containerColor = backgroundColor,
        bottomBar = {},
        topBar = { InfoTopBar(navHostController = navHostController) },

    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                if(matchedConversation?.conversationType == "PAIR"){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(color = Color.White)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(text = userInfo?.name ?: "Unknown", fontFamily = titleFont, fontSize = 20.sp)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = avatarGroupUrl,
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(color = Color.White)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(text = if (!matchedConversation?.conversationName.isNullOrBlank()) matchedConversation.conversationName
                            else "Nhóm chưa có tên", fontFamily = titleFont, fontSize = 20.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
//                    .clickable { onClick() }  // thêm sự kiện bấm
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.person),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color.White)
                            .padding(4.dp),
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Image(
                        painter = painterResource(R.drawable.bell_fill),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color.White)
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Image(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .background(color = Color.White)
                            .padding(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column (modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp)){
                FeatureButton(text = "Thay đổi chủ đề", onClick = { themePopup = true})
                Spacer(modifier = Modifier.padding(2.dp))
                FeatureButton(text = "File phương tiện", onClick = {
                    navHostController.navigate(route = "chat_media/$conversationId")
                })
                Spacer(modifier = Modifier.padding(2.dp))
                if (matchedConversation?.conversationType == "GROUP"){
                    FeatureButton(text = "Thành viên đoạn chat", onClick = {
                        navHostController.navigate(route = "group_members/$conversationId")
                    })
                    Spacer(modifier = Modifier.padding(2.dp))
                }
                FeatureButton(text = "Chặn", onClick = {})
                if (themePopup){
                    ThemePopup (onDismiss = { themePopup = false }, context = context, conversationId = conversationId, conversationViewModel = conversationViewModel)
                }
            }

        }
    }
}