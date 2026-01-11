package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.domain.repository.PeriodeWawancaraRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadJadwalWawancaraUseCase @Inject constructor(
    private val repository: PeriodeWawancaraRepository
) {
    operator fun invoke(id: Long, pdfPart: MultipartBody.Part): Flow<ResultState<PeriodeWawancaraResponse>> {
        return repository.uploadJadwal(id, pdfPart)
    }
}
