package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<ResultState<AuthResponse>>
    fun register(namaLengkap: String, email: String, password: String): Flow<ResultState<AuthResponse>>
}
