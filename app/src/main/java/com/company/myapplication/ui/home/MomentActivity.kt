package com.company.myapplication.ui.home

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.moment.MomentTopBar
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.util.topAppBarHeight
import com.company.myapplication.util.topTitleFontSize

@Composable
fun MomentScreen (
    navHostController: NavHostController,
    context: Activity
){
    val userId = UserSharedPreferences.getId(context)
    val userAvatar by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${userId}") }
    Scaffold (
        topBar = { MomentTopBar() },
        bottomBar = {
            val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route?: ""
            BottomNavigationBar(
                navController = navHostController,
                currentRoute = currentRoute,
                color = backgroundColor
            )
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = backgroundColor)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color.White)
            ){
                Image(
                    painter = rememberAsyncImagePainter(
                        model = userAvatar,
                        error = painterResource(R.drawable.person),
                        fallback = painterResource(R.drawable.person)
                    ),
                    contentDescription = "user avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(color = backgroundColor)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                )
                Text(text = "Bạn đang nghĩ gì?", modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically), color = Color.Gray)
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor)
            ){  }
        }
    }
}