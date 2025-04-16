package com.company.myapplication.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Facebook

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerTopBar() {
    TopAppBar(
        title = {
            Text("Messenger", fontWeight = FontWeight.Bold, color = Color(0xFF0084FF))
        },
        actions = {
            IconButton(onClick = { /* Navigate to Facebook */ }) {
                Icon(imageVector = FontAwesomeIcons.Brands.Facebook, contentDescription = "Facebook")
            }
        }
    )
}
