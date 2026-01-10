package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodePendaftaranResponse
import com.example.sipora.domain.repository.PeriodePendaftaranRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPeriodeByIdUseCase @Inject constructor(
    private val repository: PeriodePendaftaranRepository
) {
    operator fun invoke(id: Long): Flow<ResultState<PeriodePendaftaranResponse>> {
        return repository.getById(id)
    }
}
