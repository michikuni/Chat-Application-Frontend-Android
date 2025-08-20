package com.company.myapplication.ui.home.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.company.myapplication.R
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.util.topAppBarHeight
import com.company.myapplication.util.topAppBarPadding
import com.company.myapplication.util.topTitleFontSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTopBar(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(color = topAppBarColor)
        .height(topAppBarHeight)
        .padding(topAppBarPadding)
    ){
        CenterAlignedTopAppBar(
            title = {
                Text("Danh bạ",
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    fontFamily = titleFont,
                    fontSize = topTitleFontSize
                )
            },

            navigationIcon = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.person_plus),
                        contentDescription = null,
                        tint = themeColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}