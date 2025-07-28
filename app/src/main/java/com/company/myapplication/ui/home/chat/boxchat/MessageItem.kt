package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MessageItem (
    message: BoxChat
){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(color = Color.Transparent)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .fillMaxHeight()
                .background(color = Color.Blue, shape = RoundedCornerShape(16.dp))
        ){
            Text(message.content, color = Color.White)
        }
    }
}