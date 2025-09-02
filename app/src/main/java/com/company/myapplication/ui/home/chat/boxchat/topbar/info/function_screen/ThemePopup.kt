package com.company.myapplication.ui.home.chat.boxchat.topbar.info.function_screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.company.myapplication.util.titleFont
import com.company.myapplication.viewmodel.ConversationViewModel
import kotlinx.coroutines.launch

@Composable
fun ThemePopup(
    onDismiss: () -> Unit,
    context: Context,
    conversationId: Long,
    conversationViewModel: ConversationViewModel
) {
    val chatColorPalettes = listOf(
        listOf("0xFFFCE4EC", "0xFFF8BBD0", "0xFFF48FB1", "0xFFEC407A", "0xFFFFFFFF", "0xFFF06292", "Pastel Blush"),
        listOf("0xFFE0F7FA", "0xFF80DEEA", "0xFF26C6DA", "0xFF00838F", "0xFFFFFFFF", "0xFF4DD0E1", "Aqua Breeze"),
        listOf("0xFFFFECB3", "0xFFFFB74D", "0xFFFB8C00", "0xFFE65100", "0xFFFFF3E0", "0xFFFF9800", "Golden Sunset"),
        listOf("0xFF121212", "0xFF1E1E1E", "0xFF2C2C2C", "0xFF00E5FF", "0xFF2E2E2E", "0xFF00B8D4", "Neon Noir"),
        listOf("0xFFE8F5E9", "0xFFA5D6A7", "0xFF66BB6A", "0xFF2E7D32", "0xFFFFFFFF", "0xFF81C784", "Fresh Meadow"),
        listOf("0xFFEDE7F6", "0xFFB39DDB", "0xFF7E57C2", "0xFF5E35B1", "0xFFFFFFFF", "0xFF9575CD", "Amethyst Dream"),
        listOf("0xFF0D0D0D", "0xFF1A1A2E", "0xFF16213E", "0xFFE94560", "0xFF1F4068", "0xFFE94560", "Midnight Neon")
    )

    val scope = rememberCoroutineScope()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f) // 90% chiều cao
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Chủ đề",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = titleFont,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Xong",
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = titleFont,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(8.dp)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(chatColorPalettes) { i ->
                        val color = i
                        Column(
                            modifier = Modifier.clickable{
                                scope.launch {
                                    conversationViewModel.updateTheme(context = context, conversationId = conversationId, color = color)
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(color[0].removePrefix("0x").toLong(16)),
                                                Color(color[1].removePrefix("0x").toLong(16)),
                                                Color(color[2].removePrefix("0x").toLong(16))
                                            )
                                        )
                                    )
                                    .border(1.dp, Color.Black.copy(alpha = 0.08f),
                                        RoundedCornerShape(10.dp)),
                            )
                            Text(text = i[6], fontFamily = titleFont, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
            }
        }
    }
}
