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
import com.company.myapplication.ui.home.SettingScreen
import com.company.myapplication.ui.home.chat.boxchat.BoxChatScreen
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.InfoScreen
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen.MediaScreen
import com.company.myapplication.ui.login.LoginScreen
import com.company.myapplication.ui.register.RegisterScreen
import com.company.myapplication.ui.splash.SplashScreen
import com.company.myapplication.viewmodel.AuthViewModel
import com.company.myapplication.viewmodel.ConversationViewModel
import com.company.myapplication.viewmodel.FriendViewModel
import com.company.myapplication.viewmodel.UserViewModel

@Composable
fun AppNavigation(activity: Activity) {
    val navController = rememberNavController()
    val conversationViewModel = remember { ConversationViewModel(activity) }
    val friendViewModel = remember { FriendViewModel(activity) }
    val authViewModel = remember { AuthViewModel(activity) }
    val userViewModel = remember { UserViewModel(activity) }

    NavHost(navController = navController, startDestination = "splash") {
        composable(route = "splash") {
            SplashScreen(
                context = activity,
                navHostController = navController
            )
        }
        composable(route = "login") {
            LoginScreen(
                activity = activity,
                viewModel = authViewModel,
                onLoginSuccess = {navController.navigate("home"){
                    popUpTo("login"){ inclusive = true }
                } },
                onNavigateToRegister = { navController.navigate("register")}
            )
        }
        composable(route = "register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login")},
                onRegisterSuccess = { navController.navigate("login")}
            )
        }
        composable(route = "home") {
            HomeScreen(
                activity = activity,
                conversationViewModel = conversationViewModel,
                friendViewModel = friendViewModel,
                navHostController = navController
            )
        }

        composable(route = "contact") {
            ContactScreen(
                activity = activity,
                friendViewModel = friendViewModel,
                navHostController = navController
            )
        }

        composable("setting") {
            SettingScreen(
                navHostController = navController,
                activity = activity,
                onLogoutSuccess = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authViewModel = authViewModel,
                userViewModel = userViewModel
            )
        }

        composable(
            route = "box_chat/{contact}/{userId}/{friendId}",
            arguments = listOf(
                navArgument(name = "contact") { type = NavType.StringType },
                navArgument(name = "userId") { type = NavType.LongType },
                navArgument(name = "friendId") { type = NavType.LongType }
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
                conversationViewModel = conversationViewModel,
                activity = activity
            )
        }

        composable (
            route = "chat_friend_info/{friendId}/{userId}",
            arguments = listOf(
                navArgument(name = "friendId"){ type = NavType.LongType },
                navArgument(name = "userId"){type = NavType.LongType}
            ),

        ){backStackEntry ->
            val friendId = backStackEntry.arguments?.getLong("friendId") ?: -1
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1
            InfoScreen(
                userViewModel = userViewModel,
                friendId = friendId,
                navHostController = navController,
                userId = userId
            )
        }

        composable (
            route = "chat_media/{userId}/{friendId}",
            arguments = listOf(
                navArgument(name = "userId"){ type = NavType.LongType},
                navArgument(name = "friendId"){ type = NavType.LongType}
            )
        ){backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1
            val friendId = backStackEntry.arguments?.getLong("friendId") ?: -1
            MediaScreen(
                userId = userId,
                friendId = friendId,
                conversationViewModel = conversationViewModel,
                navHostController = navController
            )
        }
    }
}