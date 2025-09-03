package com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.ConversationViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    conversationViewModel: ConversationViewModel,
    conversationId: Long,
    navHostController: NavHostController
) {
    LaunchedEffect(Unit) {
        conversationViewModel.getAllMessage(conversationId = conversationId)
    }
    val messages by conversationViewModel.messages.collectAsState()
    Scaffold (
        topBar = {
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
                title = { Text(text = "File phương tiện", fontFamily = titleFont, fontSize = 14.sp) },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ){ paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            modifier = Modifier.padding(paddingValues)
        ){
            items(items = messages.asReversed()) { index ->
                if (index.mediaFile != null){
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize(),
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = "${ApiConfig.BASE_URL}/api/chats/getMediaFile/${index.mediaFile}"
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
