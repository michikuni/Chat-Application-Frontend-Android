package com.company.myapplication.ui.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.DisabledByDefault
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.util.topAppBarHeight
import com.company.myapplication.util.topAppBarPadding
import com.company.myapplication.util.topTitleFontSize
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.PencilAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerTopBar(
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = topAppBarColor)
            .height(topAppBarHeight)
            .padding(topAppBarPadding)
    ){
        CenterAlignedTopAppBar(
            title = {
                Text("Tin nhắn",
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    fontSize = topTitleFontSize
                    )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.DisabledByDefault,
                        contentDescription = null,
                        tint = themeColor
                    )
                }
            },
            actions = {
                IconButton(onClick = {  }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddBox,
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
