package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.ProfileService
import com.example.sipora.data.remote.request.ChangePasswordRequest
import com.example.sipora.data.remote.request.UpdateProfileRequest
import com.example.sipora.data.remote.response.AuthResponse
import com.example.sipora.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileRepository {

    override fun getProfile(): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        delay(500) // Simulate network delay
        // Mock response because the real endpoint doesn't exist
        val mockProfile = AuthResponse(
            id = 1,
            namaLengkap = "Super Admin",
            email = "superadmin@university.ac.id",
            peran = "SUPERADMIN",
            token = "dummy-token",
            type = "Bearer"
        )
        emit(ResultState.Success(mockProfile))
    }

    override fun updateProfile(namaLengkap: String): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = profileService.updateProfile(UpdateProfileRequest(namaLengkap))
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to update profile"
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun changePassword(currentPassword: String, newPassword: String): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = profileService.changePassword(ChangePasswordRequest(currentPassword, newPassword))
            if (response.isSuccessful) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to change password"
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}
