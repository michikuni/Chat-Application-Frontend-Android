package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
@Composable
fun BoxChat(
    contact: String?,
    navHostController: NavHostController
){
    var ChatMessage by remember { mutableStateOf("") }
    Scaffold (
        topBar = { TopBoxChat(contact = contact, navHostController = navHostController) },
        bottomBar = { BottomBoxChat(query =  ChatMessage, onQueryChange = {ChatMessage = it})}
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            LazyColumn (
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {

            }
        }
    }
}