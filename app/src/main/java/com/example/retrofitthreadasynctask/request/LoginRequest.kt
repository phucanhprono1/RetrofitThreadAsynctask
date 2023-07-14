package com.example.retrofitthreadasynctask.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    val password: String
    )