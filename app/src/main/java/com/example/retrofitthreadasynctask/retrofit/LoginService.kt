package com.example.retrofitthreadasynctask.retrofit

import com.example.retrofitthreadasynctask.request.LoginRequest
import com.example.retrofitthreadasynctask.request.RegisterRequest
import com.example.retrofitthreadasynctask.response.AuthenticationResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/authenticate")
    fun login(@Body request: LoginRequest): Call<AuthenticationResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthenticationResponse>
}