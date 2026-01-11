package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.request.AdministrasiRequest
import com.example.sipora.data.remote.request.JadwalWawancaraRequest
import com.example.sipora.data.remote.request.WawancaraRequest
import com.example.sipora.data.remote.response.HasilSeleksiDivisiResponse
import com.example.sipora.data.remote.response.PendaftaranResponse
import com.example.sipora.data.remote.response.UserResponse
import okhttp3.MultipartBody
import kotlinx.coroutines.flow.Flow

interface AdminRepository {
    fun getMe(): Flow<ResultState<UserResponse>>
    fun getPendaftarByOrmawa(ormawaId: Long): Flow<ResultState<List<PendaftaranResponse>>>
    fun getPendaftaranDetail(id: Long): Flow<ResultState<PendaftaranResponse>>
    fun submitAdministrasi(pendaftaranId: Long, request: AdministrasiRequest): Flow<ResultState<Unit>>
    fun submitWawancara(pendaftaranId: Long, request: WawancaraRequest): Flow<ResultState<Unit>>
    fun setJadwalWawancara(pendaftaranId: Long, request: JadwalWawancaraRequest): Flow<ResultState<Unit>>
    fun getHasilSeleksi(pendaftaranId: Long): Flow<ResultState<List<HasilSeleksiDivisiResponse>>>
    fun uploadJawabanWawancara(
        pendaftaranId: Long,
        divisiId: Long,
        file: MultipartBody.Part
    ): Flow<ResultState<HasilSeleksiDivisiResponse>>
}
