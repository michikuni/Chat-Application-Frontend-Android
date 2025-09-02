package com.company.myapplication.ui.home.chat.boxchat

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.company.myapplication.ui.home.chat.boxchat.bottombar.BottomBoxChat
import com.company.myapplication.ui.home.chat.boxchat.topbar.TopBoxChat
import com.company.myapplication.util.DataChangeHelper
import com.company.myapplication.viewmodel.ConversationViewModel
import com.google.gson.Gson

@Composable
fun BoxChatScreen(
    contact: String?,
    navHostController: NavHostController,
    userId: Long,
    friendId: Long,
    conversationViewModel: ConversationViewModel,
    activity: Activity
){
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
        conversationViewModel.getAllMessage(userId = userId, friendId = friendId)
        conversationViewModel.getAllConversation(userId = userId)
        if (dataChanged) {
            conversationViewModel.getAllMessage(userId = userId, friendId = friendId)
            DataChangeHelper.setDataChanged(activity, false)
        }
    }
    val listState = rememberLazyListState()
    val messages by conversationViewModel.messages.collectAsState()
    val conversation by conversationViewModel.conversation.collectAsState()
    val defaultColor = listOf("0xFFFFFFFF", "0xFFFFFFFF", "0xFFFFFFFF", "0xFF2196F3", "0xFF000000", "0xFFFFFFFF")

    val conversationId = messages.firstOrNull()?.conversationId?.id

    val color: List<String> = remember(conversationId, conversation) {
        val raw = conversation.firstOrNull { it.id == conversationId }?.themeColor // String? JSON như: ["0xFFF...", ...]
        val parsed = runCatching {
            if (raw.isNullOrBlank()) emptyList()
            else Gson().fromJson(raw, Array<String>::class.java).toList()
        }.getOrDefault(emptyList())

        if (parsed.size >= 3) parsed else defaultColor
    }

    for (cl in color){
        Log.e("COLOR", cl)
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(color[0].removePrefix("0x").toLong(16)),
                        Color(color[1].removePrefix("0x").toLong(16)),
                        Color(color[2].removePrefix("0x").toLong(16))
                    )
                )
            ),
        containerColor = Color.Transparent,
        topBar = {
            TopBoxChat(
                contact = contact,
                navHostController = navHostController,
                friendId = friendId,
                userId = userId,
                color = color,
                conversationId = conversationId?:-1L
            )
        },
        bottomBar = {
            BottomBoxChat(
                conversationViewModel = conversationViewModel,
                userId = userId,
                friendId = friendId,
                activity = activity,
                color = color
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(messages) { ms ->
                MessageItem(message = ms, userId = userId, color = color)
            }
        }
    }
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

}