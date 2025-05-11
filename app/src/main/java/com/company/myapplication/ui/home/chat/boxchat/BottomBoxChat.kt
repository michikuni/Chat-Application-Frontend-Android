package com.company.myapplication.ui.home.chat.boxchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.myapplication.util.searchBackgroundColor
import com.company.myapplication.util.themeColor

@Composable
fun BottomBoxChat(
    query: String,
    onQueryChange: (String) -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
            ) {
            IconButton(onClick = {},
                modifier = Modifier.weight(0.125f)) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach",
                    tint = themeColor
                )
            }

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .height(30.dp)
                    .background(color = searchBackgroundColor,
                        shape = RoundedCornerShape(16.dp))
                    .weight(0.75f)
                ,
                singleLine = false,
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                decorationBox = {innerTextField ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ){
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ){
                            if (query.isEmpty()){
                                Text(
                                    text = "Nhập tin nhắn ...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .38f),
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )

            IconButton(onClick = {},
                modifier = Modifier.weight(0.125f)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = themeColor
                )
            }
        }
        Spacer(modifier = Modifier.width(30.dp))
    }
}