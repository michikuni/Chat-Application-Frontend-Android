package com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.friendpending

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.friendpending.PendingItem
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.FriendViewModel
import kotlin.collections.forEach

@Composable
fun TabPending(
    users: List<FriendResponse>,
    onDismiss: () -> Unit,
    activity: Activity,
    friendViewModel: FriendViewModel

){
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Lời mời kết bạn",
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
        Spacer(modifier = Modifier.height(16.dp))
        Text("Gợi ý", style = MaterialTheme.typography.labelMedium, fontFamily = titleFont)

        // Giả lập danh sách liên hệ gợi ý
        val suggestedContacts = users
        suggestedContacts.forEach { request ->
            PendingItem(
                avatar = request.avatar,
                name = request.name,
                friendViewModel = friendViewModel,
                friendshipId = request.friendshipId,
                activity = activity
            )
        }
    }
}