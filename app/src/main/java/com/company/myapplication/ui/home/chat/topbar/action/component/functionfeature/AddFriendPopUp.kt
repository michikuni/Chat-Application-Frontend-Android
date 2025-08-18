package com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.company.myapplication.ui.home.util.TextField
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.themeColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.util.topAppBarColor
import com.company.myapplication.viewmodel.FriendViewModel
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun AddFriendPopUp (
    activity: Activity,
    friendViewModel: FriendViewModel,
    onDismiss: () -> Unit
){
    val userId = UserSharedPreferences.getId(activity)
    var query by remember { mutableStateOf("") }
    Dialog(onDismissRequest = {onDismiss()}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Thêm bạn",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = titleFont,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Hủy",
                        color = Color.Blue,
                        fontFamily = titleFont,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    TextField(
                        query = query,
                        onQueryChange = { query = it },
                        text = "Nhập email ...",
                        multiLine = false,
                        color = topAppBarColor,
                        modifier = Modifier.weight(0.875f).align(Alignment.CenterVertically)
                    )
                    IconButton(onClick = {
                        friendViewModel.sendAddRequest(userId = userId, receiverEmail = query.toRequestBody())
                        friendViewModel.sendAddFriendSuccess.let {
                            Toast.makeText(activity, "Gửi lời mời kết bạn thành công", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                        friendViewModel.errorMsg?.let {
                            Toast.makeText(activity, "${friendViewModel.errorMsg}", Toast.LENGTH_SHORT).show()
                        }
                    },
                        modifier = Modifier.weight(0.125f)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = themeColor
                        )
                    }
                }
            }
        }
    }
}