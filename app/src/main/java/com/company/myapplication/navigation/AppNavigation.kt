package com.company.myapplication.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.myapplication.ui.login.LoginScreen
import com.company.myapplication.ui.register.RegisterScreen
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel() }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate("home")},
                onNavigateToRegister = { navController.navigate("register")}
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login")},
                onRegisterSuccess = { navController.navigate("login")}
            )
        }
        composable("home") {
            Text("🏠 Welcome to the Home Screen!")
        }
    }
}