package com.company.myapplication.ui.home.contact

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.company.myapplication.R
import com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.AddFriendPopUp
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.util.topAppBarHeight
import com.company.myapplication.util.topAppBarPadding
import com.company.myapplication.util.topTitleFontSize
import com.company.myapplication.viewmodel.FriendViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTopBar(
    activity: Activity,
    friendViewModel: FriendViewModel,
    navHostController: NavHostController
){
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = topAppBarColor)
        .height(topAppBarHeight)
        .padding(topAppBarPadding)
    ){
        CenterAlignedTopAppBar(
            title = {
                Text("Danh bạ",
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    fontFamily = titleFont,
                    fontSize = topTitleFontSize
                )
            },

            navigationIcon = {},
            actions = {
                IconButton(onClick = {
                    showAddDialog = !showAddDialog
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.person_plus),
                        contentDescription = null,
                        tint = themeColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        if (showAddDialog){
            AddFriendPopUp(
                activity = activity,
                friendViewModel = friendViewModel,
                onDismiss = { showAddDialog = false }
            )
        }
    }
}