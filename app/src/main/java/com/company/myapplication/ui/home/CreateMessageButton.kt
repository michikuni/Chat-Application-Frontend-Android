package com.company.myapplication.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun CreateMessageButton() {
    FloatingActionButton(onClick = {
        // Navigate to new chat screen
    }) {
        Icon(Icons.Default.Edit, contentDescription = "Tạo tin nhắn")
    }
}
