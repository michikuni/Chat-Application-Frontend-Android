package com.company.myapplication.ui.home

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
import com.company.myapplication.data.model.UserChatPreview
import com.company.myapplication.ui.components.SearchBar

@Composable
fun HomeScreen(
    users: List<UserChatPreview>
){
    var searchQuery by remember { mutableStateOf("") }

    val filterUser = users.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    Scaffold (
        topBar = {
            MessengerTopBar()
        },

        bottomBar = {
            BottomNavigationBar()
        },
        floatingActionButton = {
            CreateMessageButton()
        }
    ) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)){
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it}
            )
            LazyColumn (contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)){
                items(filterUser) { user ->
                    ChatItem(user)
                }
            }
        }

    }
}