package com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.backgroundColor
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.FriendViewModel

@Composable
fun AddFriendItem(
    name: String,
    userId: Long,
    friendId: Long,
    friendViewModel: FriendViewModel,
    email: String,
    onDismiss: () -> Unit,
    context: Activity
) {
    var isSelected by remember { mutableStateOf(false) }
    var avatarUrl by remember { mutableStateOf("${ApiConfig.BASE_URL}/api/users/get_avatar/${friendId}") }
    val addFriendSuccess = friendViewModel.sendAddFriendSuccess
    val errorMsg = friendViewModel.errorMsg
    Row(
        modifier = Modifier
            .background(
                if (isSelected) Color(0xFFEEEEEE) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .clickable {
                isSelected = !isSelected
                friendViewModel.sendAddRequest(
                    userId = userId,
                    receiverEmail = email
                )
                Log.e("AddItem", "Success: $addFriendSuccess")
                Log.e("AddITem", "Fail: $errorMsg")
                addFriendSuccess?.let {
                    Toast.makeText(context, addFriendSuccess, Toast.LENGTH_SHORT).show()
                }
                errorMsg?.let {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
                onDismiss()
            }
            .padding(vertical = 10.dp),
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
                .size(50.dp)
                .clip(CircleShape)
                .background(color = backgroundColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(name, fontSize = 15.sp, fontFamily = titleFont)
    }
}