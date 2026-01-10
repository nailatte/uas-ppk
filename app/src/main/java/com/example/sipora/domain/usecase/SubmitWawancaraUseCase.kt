package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.request.WawancaraRequest
import com.example.sipora.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubmitWawancaraUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    operator fun invoke(pendaftaranId: Long, request: WawancaraRequest): Flow<ResultState<Unit>> {
        return repository.submitWawancara(pendaftaranId, request)
    }
}
