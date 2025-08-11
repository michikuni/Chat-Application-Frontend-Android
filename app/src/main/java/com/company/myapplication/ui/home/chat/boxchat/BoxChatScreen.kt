package com.company.myapplication.ui.home.chat.boxchat

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.company.myapplication.util.DataChangeHelper
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun BoxChatScreen(
    contact: String?,
    navHostController: NavHostController,
    userId: Long,
    friendId: Long,
    authViewModel: AuthViewModel,
    activity: Activity
){

    val listState = rememberLazyListState()
    val messages by authViewModel.message.collectAsState()
    val prefs = activity.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var dataChanged by remember { mutableStateOf(DataChangeHelper.hasDataChanged(activity)) }

    // Lắng nghe SharedPreferences thay đổi
    DisposableEffect(prefs) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "data_changed") {
                dataChanged = DataChangeHelper.hasDataChanged(activity)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    // Khi dataChanged = true → reload
    LaunchedEffect(dataChanged) {
        authViewModel.getAllMessage(userId = userId, friendId = friendId)
        if (dataChanged) {
            authViewModel.getAllMessage(userId = userId, friendId = friendId)
            DataChangeHelper.setDataChanged(activity, false)
        }
    }

    Scaffold(
        topBar = { TopBoxChat(contact = contact, navHostController = navHostController) },
        bottomBar = { BottomBoxChat(authViewModel = authViewModel, userId = userId, friendId = friendId) }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
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
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

}