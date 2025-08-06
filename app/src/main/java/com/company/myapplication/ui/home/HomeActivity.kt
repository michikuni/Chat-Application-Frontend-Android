package com.company.myapplication.ui.home

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.company.myapplication.ui.home.util.SearchBar
import com.company.myapplication.ui.home.util.BottomNavigationBar
import com.company.myapplication.ui.home.chat.boxchat.ChatItem
import com.company.myapplication.ui.home.chat.topboxchat.MessengerTopBar
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.lineBreakMessage
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    activity: Activity,
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    onLogoutSuccess: () -> Unit
){
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect (Unit){
        authViewModel.getAllConversation(userId)
    }

    val listConversation by authViewModel.conversation.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filterUser = listConversation.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    Scaffold (
        topBar = { MessengerTopBar(activity = activity, authViewModel = authViewModel, onLogoutSuccess = onLogoutSuccess, navHostController = navHostController) },
        bottomBar = {
            val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route?: ""
            BottomNavigationBar(navController = navHostController, currentRoute = currentRoute)
        }
    ) { paddingValues ->
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
            ) {
                items(filterUser) { user ->
                    ChatItem(user, navHostController, userId)
                    HorizontalDivider(
                        color = lineBreakMessage,
                        thickness = 0.75.dp,
                        modifier = Modifier.padding(start = 75.dp)
                    )
                }
            }
        }
    }
}