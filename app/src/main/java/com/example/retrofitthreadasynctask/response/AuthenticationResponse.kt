package com.example.retrofitthreadasynctask.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    @SerializedName("token")
    val token: String,
    val success: Boolean
)
{
    constructor():this("token",false)
}