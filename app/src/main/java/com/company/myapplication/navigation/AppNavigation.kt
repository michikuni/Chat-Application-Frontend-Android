package com.company.myapplication.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.company.myapplication.ui.home.ContactScreen
import com.company.myapplication.ui.home.HomeScreen
import com.company.myapplication.ui.home.MomentScreen
import com.company.myapplication.ui.home.SettingScreen
import com.company.myapplication.ui.home.chat.boxchat.BoxChatScreen
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.InfoScreen
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen.GroupMembers
import com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen.MediaScreen
import com.company.myapplication.ui.login.LoginScreen
import com.company.myapplication.ui.register.RegisterScreen
import com.company.myapplication.ui.splash.SplashScreen
import com.company.myapplication.viewmodel.AuthViewModel
import com.company.myapplication.viewmodel.ConversationViewModel
import com.company.myapplication.viewmodel.FeedViewModel
import com.company.myapplication.viewmodel.FriendViewModel
import com.company.myapplication.viewmodel.UserViewModel

@Composable
fun AppNavigation(activity: Activity) {
    val navController = rememberNavController()
    // ViewModels được lấy từ Hilt với scope tại Activity (graph root) để đảm bảo dùng chung
    val conversationViewModel: ConversationViewModel = hiltViewModel()
    val friendViewModel: FriendViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    val feedViewModel: FeedViewModel = hiltViewModel()

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
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable(route = "register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = { navController.navigate("login") }
            )
        }
        composable(route = "home") {
            HomeScreen(
                activity = activity,
                conversationViewModel = conversationViewModel,
                friendViewModel = friendViewModel,
                navHostController = navController,
                userViewModel = userViewModel
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
        composable("moment") {
            MomentScreen(
                navHostController = navController,
                feedViewModel = feedViewModel,
                context = activity,
                userViewModel = userViewModel
            )
        }
        composable(
            route = "box_chat/{conversationId}/{contact}",
            arguments = listOf(
                navArgument(name = "conversationId") { type = NavType.LongType },
                navArgument(name = "contact") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getLong("conversationId") ?: -1
            val contact = backStackEntry.arguments?.getString("contact") ?: ""
            BoxChatScreen(
                navHostController = navController,
                conversationId = conversationId,
                conversationViewModel = conversationViewModel,
                activity = activity,
                contact = contact
            )
        }
        composable(
            route = "chat_friend_info/{userId}/{conversationId}",
            arguments = listOf(
                navArgument(name = "conversationId") { type = NavType.LongType },
                navArgument(name = "userId") { type = NavType.LongType }
            ),
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getLong("conversationId") ?: -1
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1
            InfoScreen(
                userViewModel = userViewModel,
                navHostController = navController,
                userId = userId,
                context = activity,
                conversationViewModel = conversationViewModel,
                conversationId = conversationId
            )
        }
        composable(
            route = "chat_media/{conversationId}",
            arguments = listOf(
                navArgument(name = "conversationId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getLong("conversationId") ?: -1
            MediaScreen(
                conversationId = conversationId,
                conversationViewModel = conversationViewModel,
                navHostController = navController
            )
        }
        composable(
            route = "group_members/{conversationId}",
            arguments = listOf(
                navArgument(name = "conversationId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getLong("conversationId") ?: -1
            GroupMembers(
                conversationId = conversationId,
                conversationViewModel = conversationViewModel,
                navHostController = navController,
                context = activity,
                userViewModel = userViewModel
            )
        }
    }
}
