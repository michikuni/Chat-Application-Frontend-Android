package com.company.myapplication.data.model.auth

data class RegisterRequest(
    val name:String,
    val username:String,
    val email:String,
    val password:String
)