package com.company.myapplication.data.model.response

data class ErrorResponse(
    val message: String,
    val status: Int,
    val timestamp: Long
)