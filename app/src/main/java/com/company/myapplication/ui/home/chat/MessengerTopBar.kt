package com.company.myapplication.ui.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.company.myapplication.util.topAppBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerTopBar(
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = topAppBarColor)
            .height(50.dp)
            .padding(0.dp, 0.dp, 0.dp, 10.dp)
    ){
        val themeColor = Color(0xFF0084FF).copy(alpha = 0.6f)
        CenterAlignedTopAppBar(
            title = {
                Text("Tin nhắn", fontWeight = FontWeight.Bold, color = themeColor)
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit box chat",
                        tint = themeColor
                    )
                }
            },
            actions = {
                IconButton(onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add box chat",
                        tint = themeColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
        )
    }

}
