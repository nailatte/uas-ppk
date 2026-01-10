package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.request.ChangePasswordRequest
import com.example.sipora.data.remote.request.UpdateProfileRequest
import com.example.sipora.data.remote.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileService {

    // Placeholder for getting current user profile
    @GET("user/profile")
    suspend fun getProfile(): Response<AuthResponse>

    // Placeholder for profile update endpoint
    @PUT("user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<AuthResponse>

    // Placeholder for password change endpoint
    @PUT("user/password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Unit>
}
