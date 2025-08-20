package com.company.myapplication.ui.home

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.company.myapplication.ui.home.contact.ContactTopBar
import com.company.myapplication.ui.home.util.SearchBar
import com.company.myapplication.ui.home.contact.ContactItem
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.lineBreakMessage
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.FriendViewModel

@Composable
fun ContactScreen(
    activity: Activity,
    friendViewModel: FriendViewModel,
    navHostController: NavHostController
){
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        friendViewModel.getAllFriendsById(userId = userId)
    }
    val contacts by friendViewModel.allFriends.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filterContact = contacts.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    Scaffold (
        topBar = {ContactTopBar()},
        bottomBar = {
            val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route?: ""
            BottomNavigationBar(navController = navHostController, currentRoute = currentRoute, color = backgroundColor)
        }
        ) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)){
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                        .background(color = topAppBarColor)
            )
            LazyColumn (contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)){
                items(filterContact){ contact ->
                    ContactItem(contact)
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