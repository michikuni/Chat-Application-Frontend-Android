package com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature

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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.company.myapplication.data.model.response.FriendResponse
import com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.friendpending.TabPending
import com.company.myapplication.ui.home.chat.topbar.action.component.functionfeature.friendrequest.TabRequest
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.FriendViewModel

@Composable
fun FriendRequestPopup(
    activity: Activity,
    friendViewModel: FriendViewModel,
    onDismiss: () -> Unit
) {
    val userId = UserSharedPreferences.getId(activity)
    LaunchedEffect(Unit) {
        friendViewModel.getPendingFriendRequest(userId)
    }
    val usersPending by friendViewModel.pendingFriends.collectAsState()
    LaunchedEffect(Unit) {
        friendViewModel.getRequestFriendRequest(userId)
    }
    val usersRequest by friendViewModel.requestFriends.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Đã nhận", "Đã gửi")

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
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    contentColor = Color.LightGray,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title , fontFamily = titleFont) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTabIndex) {
                    0 -> TabPending(users = usersPending, onDismiss = onDismiss, activity = activity, friendViewModel = friendViewModel)
                    1 -> TabRequest(users = usersRequest, onDismiss = onDismiss, activity = activity, friendViewModel = friendViewModel)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}