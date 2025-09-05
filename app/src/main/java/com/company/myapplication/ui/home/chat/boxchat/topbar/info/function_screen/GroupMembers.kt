package com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.util.SearchBar
import com.company.myapplication.util.CustomMapper.toViewDTO
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.ConversationViewModel
import com.company.myapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMembers(
    conversationViewModel: ConversationViewModel,
    conversationId: Long,
    navHostController: NavHostController,
    context: Context,
    userViewModel: UserViewModel
) {
    val userId = UserSharedPreferences.getId(context = context)
    LaunchedEffect(Unit) {
        conversationViewModel.getAllConversation(userId = userId)
    }

    val allConversation by conversationViewModel.conversation.collectAsState()
    val conversation = allConversation.find { it.id == conversationId }?.toViewDTO()

    val listUser = remember { mutableStateListOf<UserResponse>() }

    conversation?.membersIds?.map { memberId ->
        userViewModel.getUserInfo(memberId)
        val user by userViewModel.user_info.collectAsState()
        user?.let { listUser.add(it) }
    }

    var searchQuery by remember { mutableStateOf("") }
    val filterUser = listUser.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }



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
                title = { Text(text = "Thành viên", fontFamily = titleFont, fontSize = 14.sp) },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .background(color = topAppBarColor)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ){
                items(items = filterUser) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(4.dp)
                            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                    ){
                        Box(modifier = Modifier.size(50.dp)){
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = "${ApiConfig.BASE_URL}/api/users/get_avatar/${user.id}",
                                    error = painterResource(R.drawable.person),
                                    fallback = painterResource(R.drawable.person)
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(color = backgroundColor)
                            )
                        }
                        Column (modifier = Modifier.align(Alignment.CenterVertically)){
                            Text(text = user.name, fontFamily = titleFont, fontSize = 14.sp)
                            Text(text = "user", fontFamily = titleFont, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
