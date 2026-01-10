package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.PeriodePendaftaranService
import com.example.sipora.data.remote.response.PeriodePendaftaranResponse
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.repository.PeriodePendaftaranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodePendaftaranRepositoryImpl @Inject constructor(
    private val service: PeriodePendaftaranService
) : PeriodePendaftaranRepository {

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

    override fun getMe(): Flow<ResultState<UserResponse>> = safeApiCall { service.getMe() }
    override fun getById(id: Long): Flow<ResultState<PeriodePendaftaranResponse>> = safeApiCall { service.getById(id) }
    override fun getByOrmawa(ormawaId: Long): Flow<ResultState<List<PeriodePendaftaranResponse>>> = safeApiCall { service.getByOrmawa(ormawaId) }
    override fun getActive(ormawaId: Long): Flow<ResultState<PeriodePendaftaranResponse>> = safeApiCall { service.getActive(ormawaId) }

    override fun create(
        namaKegiatan: String,
        deskripsi: String?,
        tanggalMulai: String,
        tanggalSelesai: String,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodePendaftaranResponse>> = safeApiCall { service.create(namaKegiatan, deskripsi, tanggalMulai, tanggalSelesai, filePart) }

    override fun update(
        id: Long,
        namaKegiatan: String?,
        deskripsi: String?,
        tanggalMulai: String?,
        tanggalSelesai: String?,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodePendaftaranResponse>> = safeApiCall { service.update(id, namaKegiatan, deskripsi, tanggalMulai, tanggalSelesai, filePart) }
}
