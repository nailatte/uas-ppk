package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PeriodeWawancaraRepository {
    fun getCurrentUser(): Flow<ResultState<UserResponse>>
    fun getActiveByOrmawa(ormawaId: Long): Flow<ResultState<PeriodeWawancaraResponse>>
    fun getListByOrmawa(ormawaId: Long): Flow<ResultState<List<PeriodeWawancaraResponse>>>
    fun createPeriode(
        namaKegiatan: String,
        deskripsi: String?,
        tanggalMulaiIso: String,
        tanggalSelesaiIso: String,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodeWawancaraResponse>>
    fun updatePeriode(
        id: Long,
        namaKegiatan: String?,
        deskripsi: String?,
        tanggalMulaiIso: String?,
        tanggalSelesaiIso: String?,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodeWawancaraResponse>>
    fun uploadJadwal(id: Long, pdfPart: MultipartBody.Part): Flow<ResultState<PeriodeWawancaraResponse>>
}
