package com.company.myapplication.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.company.myapplication.ui.home.ContactScreen
import com.company.myapplication.ui.home.HomeScreen
import com.company.myapplication.ui.home.chat.boxchat.BoxChatScreen
import com.company.myapplication.ui.login.LoginScreen
import com.company.myapplication.ui.register.RegisterScreen
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun AppNavigation(activity: Activity) {
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel(activity) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                activity = activity,
                viewModel = authViewModel,
                onLoginSuccess = {navController.navigate("home")},
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
            HomeScreen(
                activity = activity,
                authViewModel = authViewModel,
                navHostController = navController,
                onLogoutSuccess = { navController.navigate("login"){
                    popUpTo(0){ inclusive = true }
                } }
            )
        }

        composable("contact") {
            ContactScreen(
                activity = activity,
                authViewModel = authViewModel,
                navHostController = navController
            )
        }

        composable(
            "box_chat/{contact}/{userId}/{friendId}",
            arguments = listOf(
                navArgument("contact") { type = NavType.StringType },
                navArgument("userId") { type = NavType.LongType },
                navArgument("friendId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val contact = backStackEntry.arguments?.getString("contact") ?: ""
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1
            val friendId = backStackEntry.arguments?.getLong("friendId") ?: -1
            BoxChatScreen(
                contact = contact,
                navHostController = navController,
                userId = userId,
                friendId = friendId,
                authViewModel = authViewModel,
                activity = activity
            )
        }


    }
}