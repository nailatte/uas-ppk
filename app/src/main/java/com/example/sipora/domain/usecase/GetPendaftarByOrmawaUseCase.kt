package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PendaftarItemResponse
import com.example.sipora.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendaftarByOrmawaUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    operator fun invoke(ormawaId: Long): Flow<ResultState<List<PendaftarItemResponse>>> {
        return repository.getPendaftarByOrmawa(ormawaId)
    }
}
