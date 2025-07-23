package com.company.myapplication.ui.home.chat.topboxchat

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun FriendRequestPopup(
    activity: Activity,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        authViewModel.getPendingFriends(userId)
    }
    val users by authViewModel.friendsPending.collectAsState()

    Dialog(onDismissRequest = { onDismiss() }) {
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
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Lời mời kết bạn",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Hủy",
                        color = Color.Blue,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Gợi ý", style = MaterialTheme.typography.labelMedium)


                // Giả lập danh sách liên hệ gợi ý
                val suggestedContacts = users
                suggestedContacts.forEach { request ->
                    PopupRequest(
                        avatar = request.avatar,
                        name = request.name,
                        authViewModel = authViewModel,
                        friendshipId = request.friendshipId,
                        activity = activity,
                        userId = userId
                    )
                }
            }
        }
    }
}

@Composable
fun PopupRequest(
    avatar: String?,
    name: String,
    authViewModel: AuthViewModel,
    friendshipId: Long,
    activity: Activity,
    userId: Long
    //onClick: () -> Unit
){
    Column (
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Row(
        modifier = Modifier
            .fillMaxWidth()
//                .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, fontSize = 15.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .clickable { onClick() }
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                modifier = Modifier
                    .width(120.dp)
                    .height(35.dp)
                    .padding(0.dp, 0.dp, 10.dp, 0.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                onClick = {
                    authViewModel.acceptedFriendRequest(friendshipId = friendshipId)
                    authViewModel.acceptedSuccess.let {
                        Toast.makeText(activity, "Đã chấp nhận lời mời kết bạn", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Chấp nhận", fontSize = 12.sp)
            }
            Button(
                modifier = Modifier
                    .width(115.dp)
                    .height(35.dp)
                    .padding(0.dp, 0.dp, 10.dp, 0.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.DarkGray
                ),
                onClick = {}
            ) {
                Text(text = "Từ chối", fontSize = 12.sp)
            }
        }
    }
}