package com.company.myapplication.ui.home

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.repository.UserRepository
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.titleFont
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    activity: Activity
){
    val repo = UserRepository(activity)
    val userId = UserSharedPreferences.getId(activity)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri= uri
    }
    Column {
        Row (
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
                .align(alignment = Alignment.CenterHorizontally)
        ){
            Column {
                AsyncImage(
                    model = "${ApiConfig.BASE_URL}/api/users/get_avatar/$userId",
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                TextButton(onClick = { launcher.launch("image/*") }) {
                    Text(text = "Thay ảnh mới", fontFamily = titleFont)
                }

                val scope = rememberCoroutineScope()
                selectedImageUri?.let { uri ->
                    Button(onClick = {
                        scope.launch {
                            repo.uploadImage(context, uri, userId = 123L)
                        }},
                        modifier = Modifier.height(50.dp)) {
                        Text("Upload ảnh", fontFamily = titleFont)
                    }
                }
            }
        }
    }
}