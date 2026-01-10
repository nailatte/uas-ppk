package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(currentPassword: String, newPassword: String): Flow<ResultState<Unit>> {
        return repository.changePassword(currentPassword, newPassword)
    }
}
