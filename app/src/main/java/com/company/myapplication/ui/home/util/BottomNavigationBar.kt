    package com.company.myapplication.ui.home.util

    import androidx.compose.foundation.layout.size
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.AccountCircle
    import androidx.compose.material.icons.filled.Call
    import androidx.compose.material.icons.filled.Settings
    import androidx.compose.material3.Icon
    import androidx.compose.material3.NavigationBar
    import androidx.compose.material3.NavigationBarItem
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavHostController
    import compose.icons.FontAwesomeIcons
    import compose.icons.fontawesomeicons.Brands
    import compose.icons.fontawesomeicons.brands.Weixin

    @Composable
    fun BottomNavigationBar(
        navController: NavHostController,
        currentRoute: String
    ) {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(24.dp)) },
                label = { Text("Danh bạ") },
                selected = currentRoute == "contact",
                onClick = { navController.navigate("contact")}
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Call, contentDescription = null) },
                label = { Text("Gọi") },
                selected = currentRoute == "call",
                onClick = {}
            )
            NavigationBarItem(
                icon = { Icon(FontAwesomeIcons.Brands.Weixin, contentDescription = null, modifier = Modifier.size(24.dp)) },
                label = { Text("Tin nhắn") },
                selected = currentRoute == "home",
                onClick = { navController.navigate("home")}
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text("Cài đặt") },
                selected = false,
                onClick = {}
            )
        }
    }
