package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.domain.repository.PeriodeWawancaraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPeriodeWawancaraActiveUseCase @Inject constructor(
    private val repository: PeriodeWawancaraRepository
) {
    operator fun invoke(ormawaId: Long): Flow<ResultState<PeriodeWawancaraResponse>> {
        return repository.getActiveByOrmawa(ormawaId)
    }
}
