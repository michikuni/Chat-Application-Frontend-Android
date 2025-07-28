package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.company.myapplication.data.model.chat.UserChatPreview


@Composable
fun BoxChatScreen(
    contact: String?,
    navHostController: NavHostController,
    message: List<UserChatPreview>
){
    var chatMessage by remember { mutableStateOf("") }
    Scaffold (
        topBar = { TopBoxChat(contact = contact, navHostController = navHostController) },
        bottomBar = { BottomBoxChat(query =  chatMessage, onQueryChange = {chatMessage = it})}
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            LazyColumn (
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(message){message ->
                    MessageItem(message = message)
                }

            }
        }
    }
}