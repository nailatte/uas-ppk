package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.ApiService
import com.example.sipora.data.remote.request.AuthRequest
import com.example.sipora.data.remote.request.RegisterRequest
import com.example.sipora.data.remote.response.AuthResponse
import com.example.sipora.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override fun login(email: String, password: String): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val request = AuthRequest(email, password)
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown HTTP error"
                emit(ResultState.Error("HTTP Error ${response.code()}: $errorBody"))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network Error: Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun register(namaLengkap: String, email: String, password: String): Flow<ResultState<AuthResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val request = RegisterRequest(namaLengkap, email, password)
            val response = apiService.register(request)
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown HTTP error"
                emit(ResultState.Error("HTTP Error ${response.code()}: $errorBody"))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network Error: Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}
