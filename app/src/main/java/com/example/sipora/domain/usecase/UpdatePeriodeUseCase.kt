package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodePendaftaranResponse
import com.example.sipora.domain.repository.PeriodePendaftaranRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdatePeriodeUseCase @Inject constructor(
    private val repository: PeriodePendaftaranRepository
) {
    operator fun invoke(
        id: Long,
        namaKegiatan: String?,
        deskripsi: String?,
        tanggalMulai: String?,
        tanggalSelesai: String?,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodePendaftaranResponse>> {
        return repository.update(id, namaKegiatan, deskripsi, tanggalMulai, tanggalSelesai, filePart)
    }
}
