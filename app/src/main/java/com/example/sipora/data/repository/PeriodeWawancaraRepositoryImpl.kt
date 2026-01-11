package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.PeriodeWawancaraService
import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.repository.PeriodeWawancaraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeriodeWawancaraRepositoryImpl @Inject constructor(
    private val service: PeriodeWawancaraService
) : PeriodeWawancaraRepository {

    private fun <T> safeApiCall(call: suspend () -> retrofit2.Response<T>): Flow<ResultState<T & Any>> = flow {
        emit(ResultState.Loading)
        try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                emit(ResultState.Error("Error ${response.code()}: $errorBody"))
            }
        } catch (e: HttpException) {
            emit(ResultState.Error(e.message() ?: "An unexpected HTTP error occurred"))
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection and try again."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun getCurrentUser(): Flow<ResultState<UserResponse>> = safeApiCall { service.getMe() }

    override fun getActiveByOrmawa(ormawaId: Long): Flow<ResultState<PeriodeWawancaraResponse>> = safeApiCall { service.getActiveByOrmawa(ormawaId) }

    override fun getListByOrmawa(ormawaId: Long): Flow<ResultState<List<PeriodeWawancaraResponse>>> = safeApiCall { service.getListByOrmawa(ormawaId) }

    override fun createPeriode(
        namaKegiatan: String,
        deskripsi: String?,
        tanggalMulaiIso: String,
        tanggalSelesaiIso: String,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodeWawancaraResponse>> = safeApiCall {
        service.createPeriode(namaKegiatan, deskripsi, tanggalMulaiIso, tanggalSelesaiIso, filePart)
    }

    override fun updatePeriode(
        id: Long,
        namaKegiatan: String?,
        deskripsi: String?,
        tanggalMulaiIso: String?,
        tanggalSelesaiIso: String?,
        filePart: MultipartBody.Part?
    ): Flow<ResultState<PeriodeWawancaraResponse>> = safeApiCall {
        service.updatePeriode(id, namaKegiatan, deskripsi, tanggalMulaiIso, tanggalSelesaiIso, filePart)
    }

    override fun uploadJadwal(id: Long, pdfPart: MultipartBody.Part): Flow<ResultState<PeriodeWawancaraResponse>> = safeApiCall { service.uploadJadwal(id, pdfPart) }
}
