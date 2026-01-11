package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(namaLengkap: String?): Flow<ResultState<UserResponse>> {
        return repository.updateProfile(namaLengkap)
    }
}
