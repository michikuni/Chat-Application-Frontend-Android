package com.company.myapplication.ui.home.chat

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.company.myapplication.util.*
import com.company.myapplication.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerTopBar(
    activity: Activity,
    authViewModel: AuthViewModel,
    onLogoutSuccess: () -> Unit
    ) {
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
                IconButton(onClick = {
                    authViewModel.logout(activity = activity)
                    authViewModel.loginSuccess = false
                    onLogoutSuccess ()
                }) {
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
            CreateConversationPopup(activity = activity, authViewModel = authViewModel, onDismiss = { showDialog = false })
        }
    }

}


