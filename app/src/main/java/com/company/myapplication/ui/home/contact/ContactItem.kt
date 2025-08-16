package com.company.myapplication.ui.home.contact

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.company.myapplication.data.model.response.UserResponse
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
            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentDescription = null,
                model = contact.avatar
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