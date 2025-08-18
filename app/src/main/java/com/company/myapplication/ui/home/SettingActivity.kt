package com.company.myapplication.ui.home

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.company.myapplication.repository.UserRepository
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.util.FeatureButton
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    navHostController: NavHostController,
    activity: Activity,
    onLogoutSuccess: () -> Unit,
    authViewModel: AuthViewModel
){
    val repo = UserRepository(activity)
    val userId = UserSharedPreferences.getId(activity)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/$userId") }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val scope = rememberCoroutineScope()

    // ✅ Khi selectedImageUri null (tức là upload xong hoặc chưa chọn),
    // sẽ load lại ảnh từ API (avatarUrl có thêm timestamp để tránh cache)
    LaunchedEffect(selectedImageUri) {
        if (selectedImageUri == null) {
            avatarUrl = "${ApiConfig.BASE_URL}/api/users/get_avatar/$userId?ts=${System.currentTimeMillis()}"
        }
    }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (selectedImageUri != null) {
                        // ✅ Preview ảnh vừa chọn từ gallery
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "preview avatar",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        // ✅ Load avatar từ API
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                        )
                    }

                    TextButton(onClick = { launcher.launch("image/*") }) {
                        Text(text = "Thay ảnh mới", fontFamily = titleFont)
                    }

                    selectedImageUri?.let { uri ->
                        Button(
                            onClick = {
                                scope.launch {
                                    repo.uploadImage(context, uri)
                                    selectedImageUri = null // reset -> LaunchedEffect sẽ reload ảnh từ API
                                }
                            },
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text("Upload ảnh", fontFamily = titleFont)
                        }
                    }
                }
            }
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
