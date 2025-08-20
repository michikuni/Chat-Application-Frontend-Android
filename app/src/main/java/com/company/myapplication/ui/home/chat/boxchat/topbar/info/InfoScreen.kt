package com.company.myapplication.ui.home.chat.boxchat.topbar.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.UserViewModel

@Composable
fun InfoScreen(
    userViewModel: UserViewModel,
    friendId: Long
){
    LaunchedEffect(Unit) {
        userViewModel.getUserInfo(friendId)
    }
    val userInfo by userViewModel.user_info.collectAsState()
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/$friendId") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(color = Color.White)
                )

                Text(text = userInfo?.name ?: "Unknown", fontFamily = titleFont, fontSize = 20.sp)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp))
//                    .clickable { onClick() }  // thêm sự kiện bấm
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "text",
                fontFamily = titleFont,
                color = Color.Black
            )
        }
    }
}