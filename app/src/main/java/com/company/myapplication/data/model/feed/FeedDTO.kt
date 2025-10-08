package com.company.myapplication.data.model.feed

import java.time.Instant

data class FeedDTO (
    val id: Long,
    val posterId: Long,
    val content: String?,
    val createdAt: Instant,
    val mediaFile: String?,
)