package com.company.myapplication.ui.home.chat.boxchat.topbar.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.topbar.InfoTopBar
import com.company.myapplication.viewmodel.ConversationViewModel
@Composable
fun MediaScreen(
    conversationViewModel: ConversationViewModel,
    userId: Long,
    friendId: Long,
    navHostController: NavHostController
) {
    LaunchedEffect(Unit) {
        conversationViewModel.getAllMessage(userId = userId, friendId = friendId)
    }
    val messages by conversationViewModel.messages.collectAsState()

    Scaffold(
        topBar = { InfoTopBar(navHostController = navHostController) }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp), // mỗi ô ít nhất 100dp
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { ms ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "${ApiConfig.BASE_URL}/api/chats/getMediaFile/${ms.mediaFile}"
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp) // resize ảnh về 100x100
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop // crop vừa khung 100x100
                )
            }
        }
    }
}
