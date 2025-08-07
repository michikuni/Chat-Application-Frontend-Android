package com.company.myapplication.ui.home.util

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*

@Composable
fun TextField (
    query: String,
    onQueryChange: (String) -> Unit,
    text: String,
    multiLine: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    icon: (@Composable (() -> Unit))? = null
){
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(30.dp)
            .fillMaxWidth()
            .background(color = color,
                shape = RoundedCornerShape(16.dp)
            )
        ,
        singleLine = !multiLine,
        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically,
            ){
                if (icon != null){
                    Spacer(modifier = Modifier.width(5.dp))
                        icon()
                }
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }
            }

        }
    )
}