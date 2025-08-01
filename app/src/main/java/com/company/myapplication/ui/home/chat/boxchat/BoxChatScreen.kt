package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.company.myapplication.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun BoxChatScreen(
    contact: String?,
    navHostController: NavHostController,
    userId: Long,
    friendId: Long,
    authViewModel: AuthViewModel
){

    val messages by authViewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        while (true){
            authViewModel.getAllMessage(userId, friendId)
            delay(3000)
        }
    }

    Scaffold(
        topBar = { TopBoxChat(contact = contact, navHostController = navHostController) },
        bottomBar = { BottomBoxChat(authViewModel = authViewModel, userId = userId, friendId = friendId) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(messages) { ms ->
                MessageItem(message = ms, userId = userId)
            }
        }
    }

}