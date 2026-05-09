package com.company.myapplication.ui.home.contact

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.UserSharedPreferences
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.ConversationViewModel

@Composable
fun ContactItem(
    contact: UserResponse,
    context: Activity,
    navHostController: NavHostController,
    conversationViewModel: ConversationViewModel = hiltViewModel()
){
    val userId = UserSharedPreferences.getId(context = context)
    var isSelected by remember { mutableStateOf(false) }
    var conversationId by remember { mutableLongStateOf(-1) }
    LaunchedEffect(contact.id) {
        conversationId = conversationViewModel.findConversation(userId, contact.id)
    }
    Row (
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(60.dp)
            .background(
                if (isSelected) Color(0xFFEEEEEE) else Color.White
            )
            .clickable{
                isSelected = !isSelected
                navHostController.navigate("box_chat/${conversationId}/${contact.name}")
            },
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .align (Alignment.CenterVertically),
            contentDescription = null,
            painter = rememberAsyncImagePainter(
                model = "${ApiConfig.BASE_URL}/api/users/get_avatar/${contact.id}",
                error = painterResource(R.drawable.person),
                fallback = painterResource(R.drawable.person)
            )
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ){
            Text(text = contact.name,
                fontWeight = FontWeight.Bold,
                fontFamily = titleFont,
                fontSize = 14.sp)
            Box{
                Text("Online", fontSize = 12.sp, fontFamily = titleFont)
            }
        }
    }
}