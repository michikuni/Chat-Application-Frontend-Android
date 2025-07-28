package com.company.myapplication.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.myapplication.data.model.chat.UserChatPreview
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
    val message = remember { mutableStateOf<List<UserChatPreview>>(emptyList()) }
    val usersState = remember {
        mutableStateOf<List<UserChatPreview>>(emptyList())
    }
    LaunchedEffect (Unit){
        message.value = listOf(
            UserChatPreview(1, "Lê Phương Thúy", true, "Yêu thì yêu không yêu thì yêu", "avatar1.png", "19:07"),
            UserChatPreview("2", "Dũng Trần", false, "Chắc mấy câu truy vấn", "avatar2.png", "17:05"),
            UserChatPreview("3", "Học viện CSND", true, "Tài liệu ôn thi tuần sau", "avatar3.png", "16:05"),
            UserChatPreview("4", "Ngọc Anh", false, "Tối rảnh không?", "avatar4.png", "20:45"),
            UserChatPreview("5", "Hoàng Đức", true, "Làm project chưa?", "avatar5.png", "14:30"),
            UserChatPreview("6", "Trần Minh", false, "Hẹn gặp sau nhé", "avatar6.png", "11:15"),
            UserChatPreview("7", "Thu Hằng", true, "Check mail rồi nha", "avatar7.png", "10:50"),
            UserChatPreview("8", "Cường IT", false, "Đẩy lên Git chưa?", "avatar8.png", "13:00"),
            UserChatPreview("9", "Hà Linh", true, "Ảnh đẹp quá!", "avatar9.png", "09:15"),
            UserChatPreview("10", "Phạm Quốc", false, "Ngày mai học không?", "avatar10.png", "21:30"),
            UserChatPreview("11", "Mai Trang", true, "Xem bài này chưa", "avatar11.png", "18:12"),
            UserChatPreview("12", "Lộc Nguyễn", true, "Đã gửi báo cáo", "avatar12.png", "22:00"),
            UserChatPreview("13", "Đức Anh", false, "Trả lời inbox đi", "avatar13.png", "23:20"),
            UserChatPreview("14", "Trần Huy", true, "Mạng lag vãi", "avatar14.png", "15:40"),
            UserChatPreview("15", "Lê Hương", false, "Sáng mai call nhé", "avatar15.png", "08:00"),
            UserChatPreview("16", "Team Java", true, "Review code rồi", "avatar16.png", "16:00"),
            UserChatPreview("17", "Nguyễn Thảo", true, "Lên thư viện chứ?", "avatar17.png", "07:45"),
            UserChatPreview("18", "Đặng Nam", false, "Xin tài liệu", "avatar18.png", "12:30"),
            UserChatPreview("19", "Bùi Chi", true, "Làm app tới đâu rồi?", "avatar19.png", "19:10"),
            UserChatPreview("20", "Cô Giang", false, "Bài kiểm tra tuần sau", "avatar20.png", "17:50"),
            UserChatPreview("21", "Tú Anh", true, "Đã xem video rồi", "avatar21.png", "13:25"),
            UserChatPreview("22", "Minh Châu", false, "Gặp nhau chiều nay", "avatar22.png", "16:45"),
            UserChatPreview("23", "Thế Bảo", true, "Code xong chưa?", "avatar23.png", "18:10"),
            UserChatPreview("24", "Phương Uyên", true, "Cài Android Studio chưa?", "avatar24.png", "10:40"),
            UserChatPreview("25", "Khoa CNTT", false, "Lịch học đã cập nhật", "avatar25.png", "09:55"),
            UserChatPreview("26", "Tổ trưởng lớp", true, "Họp lớp 19h", "avatar26.png", "19:00"),
            UserChatPreview("27", "Trịnh Đức", false, "Bài tập nhóm đâu?", "avatar27.png", "20:20"),
            UserChatPreview("28", "Hòa Bình", true, "Gửi link Zoom", "avatar28.png", "14:10"),
            UserChatPreview("29", "Anh Quân", false, "Deadline tối nay nha", "avatar29.png", "23:45"),
            UserChatPreview("30", "Linh Lan", true, "Đang rảnh nè", "avatar30.png", "12:00")
    )
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
                users = usersState.value,
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

        composable("boxchat"){
            BoxChatScreen(
                message =
            )
        }

    }
}