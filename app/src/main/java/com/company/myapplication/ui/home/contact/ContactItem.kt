package com.company.myapplication.ui.home.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.company.myapplication.R
import com.company.myapplication.data.model.response.UserResponse
import com.company.myapplication.repository.apiconfig.ApiConfig
import com.company.myapplication.util.titleFont

@Composable
fun ContactItem(
    contact: UserResponse
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box{
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentDescription = null,
                painter = rememberAsyncImagePainter(
                    model = "${ApiConfig.BASE_URL}/api/users/get_avatar/${contact.id}",
                    error = painterResource(R.drawable.person),
                    fallback = painterResource(R.drawable.person)
                )
            )
                    }
        Spacer(modifier = Modifier.width(12.dp))

        Column (modifier = Modifier
            .weight(1f)
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