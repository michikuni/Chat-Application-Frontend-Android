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
            IconButton(onClick = {navHostController.popBackStack()}) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left_short),
                    contentDescription = null,
                    tint = Color.Black
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