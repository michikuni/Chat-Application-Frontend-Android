package com.company.myapplication.ui.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.company.myapplication.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerTopBar() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val boxWidth = (screenWidth * 0.2).dp
    val boxHeight = (screenHeight * 0.125).dp
    val paddingDropMenu = (screenWidth * 0.1).dp
    var showDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = topAppBarColor)
            .height(topAppBarHeight)
            .padding(topAppBarPadding)
    ){
        CenterAlignedTopAppBar(
            title = {
                Text("Tin nhắn",
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    fontSize = topTitleFontSize
                    )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.DisabledByDefault,
                        contentDescription = null,
                        tint = themeColor
                    )
                }
            },
            actions = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Outlined.AddBox,
                        contentDescription = "Thêm",
                        tint = themeColor)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        if (showDialog) {
            CreateConversationPopup(onDismiss = { showDialog = false })
        }
    }

}


