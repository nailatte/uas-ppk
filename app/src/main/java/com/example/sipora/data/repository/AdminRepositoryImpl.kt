package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.AdminService
import com.example.sipora.data.remote.request.AdministrasiRequest
import com.example.sipora.data.remote.request.JadwalWawancaraRequest
import com.example.sipora.data.remote.request.WawancaraRequest
import com.example.sipora.data.remote.response.*
import com.example.sipora.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val service: AdminService
) : AdminRepository {

    private fun <T> safeApiCall(call: suspend () -> Response<T>): Flow<ResultState<T>> = flow {
        emit(ResultState.Loading)
        try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                emit(ResultState.Error(response.errorBody()?.string() ?: "An error occurred"))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun getMe(): Flow<ResultState<UserMeResponse>> = safeApiCall { service.getMe() }

    override fun getPendaftarByOrmawa(ormawaId: Long): Flow<ResultState<List<PendaftarItemResponse>>> = safeApiCall { service.getPendaftarByOrmawa(ormawaId) }

    override fun getPendaftaranDetail(id: Long): Flow<ResultState<PendaftarDetailResponse>> = safeApiCall { service.getPendaftaranDetail(id) }

    override fun submitAdministrasi(pendaftaranId: Long, request: AdministrasiRequest): Flow<ResultState<Unit>> = safeApiCall { service.submitAdministrasi(pendaftaranId, request) }

    override fun submitWawancara(pendaftaranId: Long, request: WawancaraRequest): Flow<ResultState<Unit>> = safeApiCall { service.submitWawancara(pendaftaranId, request) }

    override fun setJadwalWawancara(pendaftaranId: Long, request: JadwalWawancaraRequest): Flow<ResultState<Unit>> = safeApiCall { service.setJadwalWawancara(pendaftaranId, request) }

    override fun getHasilSeleksi(pendaftaranId: Long): Flow<ResultState<HasilSeleksiResponse>> = safeApiCall { service.getHasilSeleksi(pendaftaranId) }
}
