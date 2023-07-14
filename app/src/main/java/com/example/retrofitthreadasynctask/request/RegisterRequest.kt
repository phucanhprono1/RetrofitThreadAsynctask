package com.example.retrofitthreadasynctask.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val gender: String,
    val phone: String,
    val address: String,
)