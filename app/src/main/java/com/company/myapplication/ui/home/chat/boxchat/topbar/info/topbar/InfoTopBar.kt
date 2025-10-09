package com.company.myapplication.ui.home.chat.boxchat.topbar.info.topbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.company.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTopBar (
    navHostController: NavHostController
){
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                if (navHostController.previousBackStackEntry != null) {
                    navHostController.popBackStack()
                } else {
                    // Nếu không còn màn trước thì có thể navigate về màn home hoặc xử lý khác
                    navHostController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_short),
                    contentDescription = null,
                )
            }

        },
        title = {},
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}