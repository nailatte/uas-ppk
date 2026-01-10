package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.AuthResponse
import com.example.sipora.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(namaLengkap: String, email: String, password: String): Flow<ResultState<AuthResponse>> {
        return authRepository.register(namaLengkap, email, password)
    }
}
