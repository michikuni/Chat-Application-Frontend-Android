package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.company.myapplication.util.searchBackgroundColor
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.topAppBarPadding

@Composable
fun BottomBoxChat(
    query: String,
    onQueryChange: (String) -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(searchBackgroundColor, shape = RoundedCornerShape(32.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach",
                    tint = themeColor
                )
            }

            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Nhập tin nhắn...") },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = themeColor
                )
            }
        }
    }
}