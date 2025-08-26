package com.company.myapplication.ui.home.chat.topbar.action

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.company.myapplication.ui.home.util.SearchBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.company.myapplication.ui.home.chat.topbar.action.component.FeatureItem
import com.company.myapplication.ui.home.chat.topbar.action.component.SuggestionFriend
import com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.AddFriendPopUp
import com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.FriendRequestPopup
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.FriendViewModel

@Composable
fun CreateConversationPopup(
    activity: Activity,
    friendViewModel: FriendViewModel,
    onDismiss: () -> Unit,
    navHostController: NavHostController
) {
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        friendViewModel.getAllFriendsById(userId)
    }
    val users by friendViewModel.allFriends.collectAsState()

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
                        fontFamily = titleFont,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Hủy",
                        color = Color.Blue,
                        fontFamily = titleFont,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                SearchBar(query = searchQuery,
                    onQueryChange = {searchQuery = it},)

                // Danh sách các item
                FeatureItem(
                    icon = Icons.Default.PersonAdd,
                    text = "Thêm bạn",
                    onClick = { showAddDialog = true }
                )
                FeatureItem(
                    icon = Icons.Default.GroupAdd,
                    text = "Tạo nhóm mới",
                    onClick = { showAddDialog = true }
                )
                FeatureItem(
                    icon = Icons.Default.Group,
                    text = "Lời mời kết bạn",
                    onClick = { showRequestDialog = true }
                )

                if (showRequestDialog){
                    FriendRequestPopup(
                        activity = activity,
                        friendViewModel = friendViewModel,
                        onDismiss = { showRequestDialog = false })
                }

                if (showAddDialog){
                    AddFriendPopUp(
                        activity = activity,
                        friendViewModel = friendViewModel,
                        onDismiss = { showAddDialog = false })
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Gợi ý", style = MaterialTheme.typography.labelMedium, fontFamily = titleFont)

                // Giả lập danh sách liên hệ gợi ý
                val suggestedContacts = filterUser
                suggestedContacts.forEach {
                    SuggestionFriend(
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
