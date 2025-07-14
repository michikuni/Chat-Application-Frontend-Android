package com.company.myapplication.data.model.request

data class RegisterRequest(
    val name:String,
    val account:String,
    val email:String,
    val password:String
)