package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.AuthResponse
import com.example.sipora.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<ResultState<AuthResponse>> {
        return repository.getProfile()
    }
}
