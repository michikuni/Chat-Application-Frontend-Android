package com.company.myapplication.ui.home.moment

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.viewmodel.FeedViewModel
import kotlinx.coroutines.delay

@Composable
fun PostFeedDialog(
    feedViewModel: FeedViewModel,
    userId: Long,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var content by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    val response by feedViewModel.state.collectAsState()
    val loading by feedViewModel.loading.collectAsState()

    // reset dữ liệu khi mở lại
    LaunchedEffect(Unit) {
        content = ""
        imageUri = null
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tạo bài viết mới",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Đóng",
                        color = Color.Blue,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Nhập nội dung
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Nội dung bài viết") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    maxLines = 5
                )

                Spacer(Modifier.height(12.dp))

                // Nút chọn ảnh
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = "Chọn ảnh")
                        Spacer(Modifier.width(4.dp))
                        Text("Chọn ảnh")
                    }

                    Spacer(Modifier.width(8.dp))

                    if (imageUri != null) {
                        Text(
                            text = "Đã chọn ảnh",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                imageUri?.let {
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Nút đăng bài
                Button(
                    onClick = {
                        if (content.isBlank() && imageUri == null) {
                            Toast.makeText(context, "Vui lòng nhập nội dung hoặc chọn ảnh", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        feedViewModel.postNewsFeed(context, userId, content, imageUri)
                    },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Đăng bài")
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Hiển thị phản hồi
                response?.let {
                    val textColor = if (it.message != null) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error

                    Text(
                        text = it.message ?: it.error ?: "",
                        color = textColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    // Tự đóng dialog khi đăng thành công
                    if (it.message != null) {
                        LaunchedEffect(it.message) {
                            delay(1500)
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}
