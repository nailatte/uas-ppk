package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.HasilSeleksiDivisiResponse
import com.example.sipora.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHasilSeleksiUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    operator fun invoke(pendaftaranId: Long): Flow<ResultState<List<HasilSeleksiDivisiResponse>>> {
        return repository.getHasilSeleksi(pendaftaranId)
    }
}
