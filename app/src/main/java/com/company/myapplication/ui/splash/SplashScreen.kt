package com.company.myapplication.ui.splash

import android.content.Context
import android.util.Log
import com.company.myapplication.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.company.myapplication.repository.AuthRepository
import com.company.myapplication.util.UserSharedPreferences
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    context: Context
    ) {
    val alpha = remember { Animatable(0f) }
    val token = UserSharedPreferences.getToken(context = context)?.trim()
    val api = AuthRepository(context = context)

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(1000)) // fade in 1s
        delay(1500)
        if (token != null){
            Log.e("SPLASH", token)
            val response = api.checkTokenValid()
            Log.e("SPLAH", response?.valid.toString())
            if (response?.valid == true){
                navHostController.navigate("home"){
                    popUpTo ("splash"){ inclusive = true }
                }
            } else {
                navHostController.navigate("login"){
                    popUpTo("splash"){ inclusive = true }
                }
            }

        } else {
            navHostController.navigate("login") {
                popUpTo("splash") { inclusive = true}
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alpha.value)
            )
        }
    }
}