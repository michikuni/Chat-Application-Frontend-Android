package com.company.myapplication.ui.home.setting

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import coil.compose.AsyncImage
import com.company.myapplication.repository.UserRepository
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.titleFont
import kotlinx.coroutines.launch

@Composable
fun TopSection(
    context: Context
){
    val repo = UserRepository(context = context)
    val userId = UserSharedPreferences.getId(context = context)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/$userId") }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(selectedImageUri) {
        if (selectedImageUri == null) {
            avatarUrl = "${ApiConfig.BASE_URL}/api/users/get_avatar/$userId?ts=${System.currentTimeMillis()}"
        }
    }
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
}