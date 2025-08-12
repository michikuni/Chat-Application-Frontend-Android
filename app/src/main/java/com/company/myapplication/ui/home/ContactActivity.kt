package com.company.myapplication.ui.home

import android.app.Activity
import android.util.Log
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
import com.company.myapplication.ui.home.util.BottomNavigationBar
import com.company.myapplication.ui.home.util.SearchBar
import com.company.myapplication.ui.home.contact.ContactItem
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.lineBreakMessage
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun ContactScreen(
    activity: Activity,
    authViewModel: AuthViewModel,
    navHostController: NavHostController
){
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        authViewModel.getAllFriends(userId)
    }
    val contacts by authViewModel.friends.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filterContact = contacts.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    if (contacts.isEmpty()){
        Log.e("List", "KO OK")
    }
    for (fr in contacts){
        Log.e("List", "OK4 ${fr.name}")

    }
    Log.e("ContactScreen", "OK4")
    Scaffold (
        topBar = {ContactTopBar()},
        bottomBar = {
            val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route?: ""
            BottomNavigationBar(navController = navHostController, currentRoute = currentRoute)
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