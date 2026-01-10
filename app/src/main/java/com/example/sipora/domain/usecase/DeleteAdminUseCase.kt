package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.domain.repository.SuperAdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAdminUseCase @Inject constructor(
    private val repository: SuperAdminRepository
) {
    operator fun invoke(adminId: Long): Flow<ResultState<Unit>> {
        return repository.deleteAdmin(adminId)
    }
}
