package com.company.myapplication.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.company.myapplication.data.model.chat.UserChatPreview
import com.company.myapplication.data.model.user.UserRespone
import com.company.myapplication.ui.home.contact.ContactTopBar
import com.company.myapplication.ui.home.util.BottomNavigationBar
import com.company.myapplication.ui.home.util.SearchBar
import com.company.myapplication.ui.home.contact.ContactItem
import com.company.myapplication.util.lineBreakMessage
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun ContactScreen(
    authViewModel: AuthViewModel,
    navHostController: NavHostController
){
    Log.e("ContactScreen", "OK")
    val userId = authViewModel.userId
    Log.e("ContactScreen", "OK $userId")
    if(userId != null){
        LaunchedEffect(userId) {
            authViewModel.getAllFriends(userId)
        }
    } else{
        Log.e("ContactScreen", "Khoong ther timt hay ngwofi dung")
    }
    Log.e("ContactScreen", "OK2")

    val contacts by authViewModel.friends.collectAsState()
    Log.e("ContactScreen", "OK1")

    for (fr in contacts){
        Log.e("ContactScreen", "List ${fr.name}")
    }
    Log.e("ContactScreen", "OK3")

    var searchQuery by remember { mutableStateOf("") }
    val filterContact = contacts.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
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
                onQueryChange = { searchQuery = it }
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