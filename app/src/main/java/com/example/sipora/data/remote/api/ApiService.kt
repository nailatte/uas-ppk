package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.request.AuthRequest
import com.example.sipora.data.remote.request.RegisterRequest
import com.example.sipora.data.remote.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body req: AuthRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body req: RegisterRequest): Response<AuthResponse>

}
