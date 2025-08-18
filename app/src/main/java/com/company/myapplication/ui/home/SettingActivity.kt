package com.company.myapplication.ui.home

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.company.myapplication.ui.home.setting.FeatureButton
import com.company.myapplication.ui.home.setting.TopSection
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun SettingScreen(
    navHostController: NavHostController,
    activity: Activity,
    onLogoutSuccess: () -> Unit,
    authViewModel: AuthViewModel
){
    Scaffold (
        bottomBar = {
            val currentBackStackEntry = navHostController.currentBackStackEntryAsState().value
            val currentRoute = currentBackStackEntry?.destination?.route?: ""
            BottomNavigationBar(navController = navHostController, currentRoute = currentRoute)
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
           TopSection(context = activity)
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 0.dp)
            ) {
                FeatureButton(text = "Trang cá nhân", onClick = {})
                Spacer(modifier = Modifier.padding(2.dp))
                FeatureButton(text = "Đăng xuất", onClick = {
                    authViewModel.logout(activity = activity)
                    authViewModel.loginSuccess = false
                    onLogoutSuccess ()
                })
            }
        }
    }
}
