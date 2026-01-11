package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<ResultState<UserResponse>>
    fun updateProfile(namaLengkap: String?): Flow<ResultState<UserResponse>>
    fun changePassword(newPassword: String): Flow<ResultState<UserResponse>>
}
