package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.AdminResponse
import com.example.sipora.domain.repository.SuperAdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateAdminUseCase @Inject constructor(
    private val repository: SuperAdminRepository
) {
    operator fun invoke(namaLengkap: String, email: String, password: String): Flow<ResultState<AdminResponse>> {
        return repository.createAdmin(namaLengkap, email, password)
    }
}
