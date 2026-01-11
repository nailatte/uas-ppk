package com.example.sipora.domain.usecase

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.domain.repository.PeriodeWawancaraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePeriodeWawancaraUseCase @Inject constructor(
    private val repository: PeriodeWawancaraRepository
) {
    operator fun invoke(
        namaKegiatan: String,
        deskripsi: String?,
        tanggalMulaiIso: String,
        tanggalSelesaiIso: String,
        filePart: okhttp3.MultipartBody.Part?
    ): Flow<ResultState<PeriodeWawancaraResponse>> {
        return repository.createPeriode(namaKegiatan, deskripsi, tanggalMulaiIso, tanggalSelesaiIso, filePart)
    }
}
