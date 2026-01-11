package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.request.UpdateProfileRequest
import com.example.sipora.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileService {

    @GET("users/me")
    suspend fun getProfile(): Response<UserResponse>

    @PUT("users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserResponse>
}
