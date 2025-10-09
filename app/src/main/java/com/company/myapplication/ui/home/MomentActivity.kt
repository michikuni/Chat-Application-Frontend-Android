package com.company.myapplication.ui.home

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.moment.PostCard
import com.company.myapplication.ui.home.moment.MomentTopBar
import com.company.myapplication.ui.home.moment.PostFeedDialog
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.FeedViewModel
import com.company.myapplication.viewmodel.UserViewModel

@Composable
fun MomentScreen(
    navHostController: NavHostController,
    context: Activity,
    feedViewModel: FeedViewModel,
    userViewModel: UserViewModel
) {
    Log.d("MomentScreen", "🧭 Mở MomentScreen")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        var showDialog by remember { mutableStateOf(false) }
        val userId = UserSharedPreferences.getId(context)
        val userAvatar by remember {
            mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${userId}")
        }

        // Khi mở màn hình thì load dữ liệu
        LaunchedEffect(Unit) {
            Log.e("MomentScreen", "📡 Gọi feedViewModel.getAllFeed($userId)")
            feedViewModel.getAllFeed(userId)
        }
        val allFeed by feedViewModel.allFeed.collectAsState()

        LaunchedEffect(Unit) {
            Log.e("MomentScreen", "📡 Gọi userViewModel.getUserInfo($userId)")
            userViewModel.getUserInfo(userId)
        }
        val userInfo by userViewModel.userInfo.collectAsState()

        // Log khi dữ liệu thay đổi
        LaunchedEffect(allFeed) {
            Log.e("MomentScreen", "🧾 allFeed cập nhật (${allFeed.size} phần tử):")
            allFeed.forEachIndexed { index, feed ->
                Log.e(
                    "MomentScreen",
                    "   [${index + 1}] id=${feed.id}, caption=${feed.content}, mediaFile=${feed.mediaFile}"
                )
            }
        }

        LaunchedEffect(userInfo) {
            Log.e("MomentScreen", "👤 userInfo cập nhật:")
            Log.e("MomentScreen", "   id=${userInfo?.id}")
            Log.e("MomentScreen", "   name=${userInfo?.name}")
            Log.e("MomentScreen", "   email=${userInfo?.email}")
            Log.e("MomentScreen", "   avatar=${userInfo?.avatar}")
        }

        if (showDialog) {
            PostFeedDialog(
                feedViewModel = feedViewModel,
                userId = userId,
                onDismiss = { showDialog = false }
            )
        }

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = { MomentTopBar() },
            bottomBar = {
                val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
                val currentRoute = currentBackStackEntry?.destination?.route ?: ""
                BottomNavigationBar(
                    navController = navHostController,
                    currentRoute = currentRoute,
                    color = backgroundColor
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = topAppBarColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.White)
                        .clickable { showDialog = true }
                ) {
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
                    Text(
                        text = "Bạn đang nghĩ gì?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically),
                        color = Color.Gray
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(allFeed) { feed ->
                        Log.e("MomentScreen", "🧩 Hiển thị feed id=${feed.id}, caption=${feed.content}")
                        PostCard(
                            userInfo = userInfo,
                            feedInfo = feed
                        )
                    }
                }
            }
        }
    }
}