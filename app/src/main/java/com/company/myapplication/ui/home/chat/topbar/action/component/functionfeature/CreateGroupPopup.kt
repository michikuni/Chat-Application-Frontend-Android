package com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.company.myapplication.data.model.chat.CreateConversationGroup
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.ConversationRepository
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.ui.home.util.SearchBar
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.R
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.titleFont
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupPopup (
    context: Context,
    onDismiss: () -> Unit,
    friends: List<UserResponse>
){
    var groupName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    val userId = UserSharedPreferences.getId(context = context)
    val selectedUsers = remember { mutableStateListOf<UserResponse>() }
    val memberIds: List<Long> = listOf(userId) + selectedUsers.map { it.id }
    val repo = ConversationRepository(context = context)
    val scope = rememberCoroutineScope()
    val filteredUsers = friends.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val file = drawableToFile(context, R.drawable.people)
    val uri = file.toUri()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f) // 90% chiều cao
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text("Nhóm mới",
                                fontFamily = titleFont)
                        },
                        navigationIcon = {
                            Text(
                                text = "Hủy",
                                color = Color.Blue,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { onDismiss() },
                                fontFamily = titleFont
                            )
                        },
                        actions = {
                            Text(
                                text = "Tạo",
                                color = if (selectedUsers.isNotEmpty()) Color.Blue else Color.Gray,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable(enabled = selectedUsers.isNotEmpty()) {
                                        scope.launch {
                                            repo.createConversationGroup(
                                                data = CreateConversationGroup(
                                                    members = memberIds,
                                                    name = groupName),
                                                uri = selectedImageUri ?: uri,
                                                context = context
                                            )
                                        }
                                    },
                                fontFamily = titleFont
                            )
                        }
                    )
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    ){
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = selectedImageUri,
                                error = painterResource(R.drawable.photo_camera_20px),
                                fallback = painterResource(R.drawable.photo_camera_20px)
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(color = backgroundColor)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                        BasicTextField(
                            value = groupName,
                            onValueChange = { groupName = it },
                            modifier = Modifier
                                .height(64.dp)
                                .background(color = Color.Transparent)
                            ,
                            singleLine = false,
                            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                Row(verticalAlignment = Alignment.CenterVertically,
                                ){
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (groupName.isEmpty()) {
                                            Text(
                                                text = "Tên nhóm (Không bắt buộc)",
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                                fontSize = 14.sp,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontFamily = titleFont
                                            )
                                        }
                                        innerTextField()
                                    }
                                }

                            }
                        )
                    }

                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier
                            .background(color = topAppBarColor)
                    )

                    Text(
                        "Gợi ý",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontFamily = titleFont
                    )

                    LazyColumn {
                        items(filteredUsers) { user ->
                            UserItem(
                                user = user,
                                isSelected = user in selectedUsers,
                                onClick = {
                                    if (user in selectedUsers) {
                                        selectedUsers.remove(user)
                                    } else {
                                        selectedUsers.add(user)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun UserItem(
    user: UserResponse,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${user.id}") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = avatarUrl,
                error = painterResource(R.drawable.person),
                fallback = painterResource(R.drawable.person)
            ),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color = backgroundColor)
        )
        Spacer(Modifier.width(12.dp))
        Text(user.name, modifier = Modifier.weight(1f), fontFamily = titleFont)
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() }
        )
    }
}
fun drawableToFile(context: Context, drawableId: Int, fileName: String = "temp.png"): File {
    val file = File(context.cacheDir, fileName)
    val inputStream = context.resources.openRawResource(drawableId)
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)
    outputStream.close()
    inputStream.close()
    return file
}