package com.company.myapplication.ui.home.chat

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun CreateConversationPopup(
    activity: Activity,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        authViewModel.getAllFriends(userId)
    }
    val users by authViewModel.friends.collectAsState()
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
                    PopupItem(icon = Icons.Default.PersonAdd, text = "Thêm bạn")
                    PopupItem(icon = Icons.Default.GroupAdd, text = "Tạo nhóm mới")

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Gợi ý", style = MaterialTheme.typography.labelMedium)

                    // Giả lập danh sách liên hệ gợi ý
                    val suggestedContacts = filterUser
                    suggestedContacts.forEach {
                        PopupSuggestion(name = it.name)
                    }
                }
            }
        }
    }

    @Composable
    fun PopupItem(icon: ImageVector, text: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Xử lý logic */ }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 16.sp)
        }
    }

    @Composable
    fun PopupSuggestion(name: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Mở chat */ }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, fontSize = 15.sp)
        }
    }

