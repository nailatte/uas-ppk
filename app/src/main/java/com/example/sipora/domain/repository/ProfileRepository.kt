package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.AuthResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<ResultState<AuthResponse>>
    fun updateProfile(namaLengkap: String): Flow<ResultState<AuthResponse>>
    fun changePassword(currentPassword: String, newPassword: String): Flow<ResultState<Unit>>
}
