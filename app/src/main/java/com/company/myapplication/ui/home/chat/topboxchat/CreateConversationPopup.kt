package com.company.myapplication.ui.home.chat.topboxchat

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.company.myapplication.ui.home.util.SearchBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun CreateConversationPopup(
    activity: Activity,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    navHostController: NavHostController
) {
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        authViewModel.getAllFriends(userId)
    }
    val users by authViewModel.friends.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    var showRequestDialog by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }

    val filterUser = users.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tin nhắn mới",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Hủy",
                        color = Color.Blue,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                SearchBar(query = searchQuery,
                    onQueryChange = {searchQuery = it},)

                // Danh sách các item
                PopupItem(
                    icon = Icons.Default.PersonAdd,
                    text = "Thêm bạn",
                    onClick = { showAddDialog = true}
                )
                PopupItem(
                    icon = Icons.Default.GroupAdd,
                    text = "Tạo nhóm mới",
                    onClick = { showAddDialog = true}
                )
                PopupItem(
                    icon = Icons.Default.Group,
                    text = "Lời mời kết bạn",
                    onClick = { showRequestDialog = true}
                )

                if (showRequestDialog){
                    FriendRequestPopup (activity = activity, authViewModel = authViewModel, onDismiss = { showRequestDialog = false })
                }

                if (showAddDialog){
                    AddFriendPopUp(activity = activity, authViewModel = authViewModel, onDismiss = { showAddDialog = false })
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Gợi ý", style = MaterialTheme.typography.labelMedium)

                // Giả lập danh sách liên hệ gợi ý
                val suggestedContacts = filterUser
                suggestedContacts.forEach {
                    PopupSuggestion(
                        avatar = it.avatar,
                        name = it.name,
                        userId = userId,
                        friendId = it.id,
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}

    @Composable
    fun PopupItem(
        icon: ImageVector,
        text: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 16.sp)
        }
    }

    @Composable
    fun PopupSuggestion(
        avatar: String?,
        name: String,
        userId: Long,
        friendId: Long,
        navHostController: NavHostController
    ) {
        var isSelected by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isSelected = !isSelected
                    navHostController.navigate("box_chat/${name}/${userId}/${friendId}")
                }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, fontSize = 15.sp)
        }
    }

