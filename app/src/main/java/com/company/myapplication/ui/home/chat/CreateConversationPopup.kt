package com.company.myapplication.ui.home.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun CreateConversationPopup(
        onDismiss: () -> Unit
    ) {
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Danh sách các item
                    PopupItem(icon = Icons.Default.Group, text = "Tạo nhóm mới")
                    PopupItem(icon = Icons.Default.ChatBubbleOutline, text = "Cộng đồng")
                    PopupItem(icon = Icons.Default.Face, text = "Chat với AI")

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Gợi ý", style = MaterialTheme.typography.labelMedium)

                    // Giả lập danh sách liên hệ gợi ý
                    val suggestedContacts = listOf("Đặng Bích Duyên", "Hoàng Bảo Khanh", "Linh Đào")
                    suggestedContacts.forEach {
                        PopupSuggestion(name = it)
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

