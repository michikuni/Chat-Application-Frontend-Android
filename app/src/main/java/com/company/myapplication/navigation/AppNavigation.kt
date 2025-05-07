package com.company.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.myapplication.data.model.chat.UserChatPreview
import com.company.myapplication.ui.home.HomeScreen
import com.company.myapplication.ui.login.LoginScreen
import com.company.myapplication.ui.register.RegisterScreen
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel() }
    val usersState = remember {
        mutableStateOf<List<UserChatPreview>>(emptyList())
    }
    LaunchedEffect(Unit) {
        // TODO: Gọi API thật tại đây nếu có
        usersState.value = listOf(
            UserChatPreview(1, "Lê Phương Thúy", "yêu thì", "19:07", "facebook", true),
            UserChatPreview(2, "Dũng Trần", "chắc mấy câu truy vấn", "17:05", "mess", true),
            UserChatPreview(3, "Học viện CSND", "tài liệu ôn thi", "16:05", "hvna", true),
            UserChatPreview(4, "Ngọc Anh", "tối rảnh không?", "20:45", "ngocanh", false),
            UserChatPreview(5, "Hoàng Đức", "làm project chưa?", "14:30", "hoangduc", true),
            UserChatPreview(6, "Trần Minh", "hẹn gặp sau nhé", "11:15", "tranminh", false),
            UserChatPreview(7, "Thu Hằng", "check mail rồi nha", "10:50", "thuhang", true),
            UserChatPreview(8, "Cường IT", "đẩy lên Git chưa?", "13:00", "cuongit", false),
            UserChatPreview(9, "Hà Linh", "ảnh đẹp quá!", "09:15", "halinh", true),
            UserChatPreview(10, "Phạm Quốc", "ngày mai học không?", "21:30", "phamquoc", false),
            UserChatPreview(11, "Mai Trang", "xem bài này chưa", "18:12", "maitrang", true),
            UserChatPreview(12, "Lộc Nguyễn", "đã gửi báo cáo", "22:00", "locnguyen", true),
            UserChatPreview(13, "Đức Anh", "trả lời inbox đi", "23:20", "ducanh", false),
            UserChatPreview(14, "Trần Huy", "mạng lag vãi", "15:40", "tranhuy", true),
            UserChatPreview(15, "Lê Hương", "sáng mai call nhé", "08:00", "lehuong", false),
            UserChatPreview(16, "Team Java", "review code rồi", "16:00", "teamjava", true),
            UserChatPreview(17, "Nguyễn Thảo", "lên thư viện chứ?", "07:45", "nguyenthao", true),
            UserChatPreview(18, "Đặng Nam", "xin tài liệu", "12:30", "dangnam", false),
            UserChatPreview(19, "Bùi Chi", "làm app tới đâu rồi?", "19:10", "buichi", true),
            UserChatPreview(20, "Cô Giang", "bài kiểm tra tuần sau", "17:50", "cogiang", false)
        )
    }

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
            HomeScreen(
                users = usersState.value
            )
        }
    }
}