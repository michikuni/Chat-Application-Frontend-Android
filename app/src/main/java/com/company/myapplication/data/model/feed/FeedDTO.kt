package com.company.myapplication.data.model.feed

import java.sql.Timestamp
import java.time.Instant

data class FeedDTO (
    val id: Long,
    val posterId: Long,
    val content: String?,
    val createdAt: String,
    val mediaFile: String?,
)
// Data class này đại diện cho tất cả trạng thái của màn hình Feed
data class FeedScreenUiState(
    val isLoading: Boolean = false,
    val feeds: List<FeedDTO> = emptyList(),
    val userMessage: String? = null, // Dùng để hiển thị lỗi hoặc thông báo thành công
    val postSuccess: Boolean = false // Cờ để báo hiệu post bài thành công
)