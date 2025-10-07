package com.company.myapplication.ui.home.chat.topbar

import android.app.Activity
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.company.myapplication.R
import com.company.myapplication.ui.home.chat.topbar.action.CreateConversationPopup
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.util.topAppBarHeight
import com.company.myapplication.util.topAppBarPadding
import com.company.myapplication.util.topTitleFontSize
import com.company.myapplication.viewmodel.FriendViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    activity: Activity,
    friendViewModel: FriendViewModel,
    navHostController: NavHostController
) {
    var showCreateConversationDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = topAppBarColor)
            .height(topAppBarHeight)
            .padding(topAppBarPadding)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Tin nhắn",
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    fontSize = topTitleFontSize,
                    fontFamily = titleFont
                )
            },
            navigationIcon = { /* Không có icon trái */ },
            actions = {
                IconButton(onClick = { showCreateConversationDialog = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.pencil_square),
                        contentDescription = "Tạo cuộc trò chuyện mới",
                        tint = themeColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = topAppBarColor
            )
        )
    }

    // Hiển thị popup ở ngoài Box để tránh giới hạn bố cục
    if (showCreateConversationDialog) {
        CreateConversationPopup(
            activity = activity,
            friendViewModel = friendViewModel,
            onDismiss = { showCreateConversationDialog = false },
            navHostController = navHostController
        )
    }
}
