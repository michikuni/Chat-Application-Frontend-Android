package com.company.myapplication.ui.home.util

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val tfWidth = (screenWidth / 10 * 7.5)
    Box (
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        Alignment.Center
    ){
        Row (
            modifier = modifier
                .width(tfWidth.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(query = query,
                onQueryChange = onQueryChange,
                text = "Tìm kiếm",
                multiLine = false,
                color = Color.White,
                icon = { SearchIcon() }
            )
        }
    }
}
