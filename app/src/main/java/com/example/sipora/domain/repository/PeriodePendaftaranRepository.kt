package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.PeriodePendaftaranResponse
import com.example.sipora.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PeriodePendaftaranRepository {
    fun getMe(): Flow<ResultState<UserResponse>>
    fun getById(id: Long): Flow<ResultState<PeriodePendaftaranResponse>>
    fun getByOrmawa(ormawaId: Long): Flow<ResultState<List<PeriodePendaftaranResponse>>>
    fun getActive(ormawaId: Long): Flow<ResultState<PeriodePendaftaranResponse>>
    fun create(
        namaKegiatan: String,
        deskripsi: String?,
        tanggalMulai: String,
        tanggalSelesai: String,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodePendaftaranResponse>>
    fun update(
        id: Long,
        namaKegiatan: String?,
        deskripsi: String?,
        tanggalMulai: String?,
        tanggalSelesai: String?,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodePendaftaranResponse>>
}
