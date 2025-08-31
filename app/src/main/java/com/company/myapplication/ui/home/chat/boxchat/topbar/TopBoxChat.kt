package com.company.myapplication.ui.home.chat.boxchat.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.company.myapplication.R
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarHeight
import com.company.myapplication.util.topAppBarPadding
import com.company.myapplication.util.topTitleBoxChatFontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBoxChat(
    contact: String?,
    navHostController: NavHostController,
    friendId: Long,
    userId: Long,
    color: List<String>,
    conversationId: Long
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .height(topAppBarHeight)
            .padding(topAppBarPadding)
    ){
        CenterAlignedTopAppBar(
            title = {
                if (contact != null) {
                    Text(contact,
                        fontWeight = FontWeight.Bold,
                        color = Color(color[3].removePrefix("0x").toLong(16)),
                        fontSize = topTitleBoxChatFontSize,
                        fontFamily = titleFont
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {navHostController.popBackStack()}) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_short),
                        contentDescription = null,
                        tint = Color(color[3].removePrefix("0x").toLong(16))
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    navHostController.navigate(route = "chat_friend_info/$friendId/$userId/$conversationId")
                }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info_circle),
                        contentDescription = null,
                        tint = Color(color[3].removePrefix("0x").toLong(16))
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}