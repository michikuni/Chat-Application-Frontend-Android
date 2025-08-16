package com.company.myapplication.ui.login

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.company.myapplication.R
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    activity: Activity,
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "Đăng nhập", style = MaterialTheme.typography.headlineMedium, fontFamily = titleFont)

        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Email", fontFamily = titleFont) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Mật khẩu", fontFamily = titleFont) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login(activity, username, password)
                      },
            modifier = Modifier.fillMaxWidth().height(45.dp))
        {
            Text("Đăng nhập", fontFamily = titleFont)
        }

        if (viewModel.loginSuccess){
            Text("Đăng nhập thành công!", color = Color.Green, fontFamily = titleFont)
            LaunchedEffect(Unit) {
                onLoginSuccess()
            }
        }

        viewModel.errorMessage?.let {
            Text(text = it, color = Color.Red, fontFamily = titleFont)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {  }) {
            Text(text = "Quên mật khẩu", fontFamily = titleFont)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onNavigateToRegister() },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .align(alignment = Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,        // Nền trắng
                contentColor = Color(0xFF673AB7)     // Màu chữ tím
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFF673AB7)            // Viền tím
            ),
            shape = RoundedCornerShape(50),       // Bo tròn toàn bộ
        ) {
            Text(
                text = "Tạo tài khoản mới",
                fontFamily = titleFont,
                color = Color(0xFF673AB7)             // Chữ tím
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


    }
}