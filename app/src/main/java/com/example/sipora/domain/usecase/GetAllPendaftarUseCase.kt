package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PendaftarResponse
import com.example.sipora.domain.repository.SuperAdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPendaftarUseCase @Inject constructor(
    private val repository: SuperAdminRepository
) {
    operator fun invoke(): Flow<ResultState<List<PendaftarResponse>>> {
        return repository.getAllPendaftar()
    }
}
