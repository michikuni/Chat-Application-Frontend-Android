package com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.friendpending

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.FriendViewModel

@Composable
fun PendingItem(
    avatar: String?,
    name: String,
    friendViewModel: FriendViewModel,
    friendshipId: Long,
    activity: Activity
){
    Column (
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
            Text(name, fontSize = 15.sp, fontFamily = titleFont)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    friendViewModel.acceptedFriendRequest(friendshipId = friendshipId)
                    friendViewModel.acceptedFriendSuccess.let {
                        Toast.makeText(activity, "Đã chấp nhận lời mời kết bạn", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Chấp nhận", fontSize = 12.sp, fontFamily = titleFont)
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
                onClick = {
                    friendViewModel.cancelFriendRequest(friendshipId)
                    friendViewModel.canceledFriendSuccess.let {
                        Toast.makeText(activity, "Đã từ chối lời mời kết bạn", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Từ chối", fontSize = 12.sp, fontFamily = titleFont)
            }
        }
    }
}