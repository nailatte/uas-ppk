package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.ProfileService
import com.example.sipora.data.remote.request.UpdateProfileRequest
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileService: ProfileService
) : ProfileRepository {

    override fun getProfile(): Flow<ResultState<UserResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = profileService.getProfile()
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to load profile"
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun updateProfile(namaLengkap: String?): Flow<ResultState<UserResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = profileService.updateProfile(UpdateProfileRequest(namaLengkap = namaLengkap))
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

    override fun changePassword(newPassword: String): Flow<ResultState<UserResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = profileService.updateProfile(UpdateProfileRequest(password = newPassword))
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
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
