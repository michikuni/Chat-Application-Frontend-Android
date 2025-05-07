package com.company.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.company.myapplication.util.searchBackgroundColor

import com.company.myapplication.util.topAppBarColor

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = topAppBarColor)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ){
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = modifier
                .width(300.dp)
                .height(30.dp)
                .background(color = searchBackgroundColor, shape = RoundedCornerShape(16.dp))
            ,
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (query.isEmpty()){
                        Text("Search", color = Color.Gray, fontSize = 16.sp)
                    } else {
                        innerTextField()
                    }
                }

            }
        )
    }
}
