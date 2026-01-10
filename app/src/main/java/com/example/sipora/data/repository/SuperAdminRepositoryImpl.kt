package com.example.sipora.data.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.api.SuperAdminService
import com.example.sipora.data.remote.response.* 
import com.example.sipora.domain.repository.SuperAdminRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuperAdminRepositoryImpl @Inject constructor(
    private val superAdminService: SuperAdminService
) : SuperAdminRepository {

    override fun getAllAdmins(): Flow<ResultState<List<AdminResponse>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = superAdminService.getAllAdmins()
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to fetch admins"
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun createAdmin(namaLengkap: String, email: String, password: String): Flow<ResultState<AdminResponse>> = flow {
        emit(ResultState.Loading)
        try {
            val response = superAdminService.createAdmin(namaLengkap, email, password)
            if (response.isSuccessful && response.body() != null) {
                emit(ResultState.Success(response.body()!!))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to create admin"
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun deleteAdmin(adminId: Long): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading)
        try {
            val response = superAdminService.deleteAdmin(adminId)
            if (response.isSuccessful) {
                emit(ResultState.Success(Unit))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Failed to delete admin"
                emit(ResultState.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(ResultState.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun getStatistics(): Flow<ResultState<List<StatisticResponse>>> = flow {
       // This is now deprecated and will be calculated on the fly
    }

    override fun getAllPendaftar(): Flow<ResultState<List<PendaftarResponse>>> = flow {
        emit(ResultState.Loading)
        delay(1000) // Simulate network delay
        val mockPendaftar = listOf(
            PendaftarResponse(1, "Mahasiswa A", "BEM", "LOLOS", "PENDING"),
            PendaftarResponse(2, "Mahasiswa B", "BEM", "GAGAL", "PENDING"),
            PendaftarResponse(3, "Mahasiswa C", "DPM", "LOLOS", "LOLOS"),
            PendaftarResponse(4, "Mahasiswa D", "BEM", "LOLOS", "GAGAL"),
            PendaftarResponse(5, "Mahasiswa E", "DPM", "PENDING", "PENDING")
        )
        emit(ResultState.Success(mockPendaftar))
    }
}
