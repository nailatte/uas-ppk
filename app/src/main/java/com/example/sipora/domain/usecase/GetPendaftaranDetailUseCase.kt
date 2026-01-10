package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PendaftarDetailResponse
import com.example.sipora.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendaftaranDetailUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    operator fun invoke(id: Long): Flow<ResultState<PendaftarDetailResponse>> {
        return repository.getPendaftaranDetail(id)
    }
}
