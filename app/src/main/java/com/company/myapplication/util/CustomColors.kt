package com.company.myapplication.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.myapplication.R

val backgroundColor = Color(red = 244, green = 243, blue = 248)
val buttonColor = Color.White
val topAppBarColor = Color.Transparent //Top
val searchBackgroundColor = Color(red = 244, green = 243, blue = 248)
val lineBreakMessage = Color(0xFFAFAFAF)
val themeColor = Color(0xFF0084FF).copy(alpha = 0.6f)
val topAppBarHeight = 50.dp
val topAppBarPadding = PaddingValues(0.dp, 10.dp, 0.dp, 10.dp)
val topTitleFontSize = 28.sp
val topTitleBoxChatFontSize = 20.sp
val titleFont = FontFamily(
    Font(R.font.libertinus_sans_regular, FontWeight.Normal),
    Font(R.font.libertinus_sans_bold, FontWeight.Bold),
    Font(R.font.libertinus_sans_italic, FontWeight.Thin)
)