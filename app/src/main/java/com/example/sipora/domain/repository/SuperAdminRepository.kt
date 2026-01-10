package com.example.sipora.domain.repository

import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.response.AdminResponse
import com.example.sipora.data.remote.response.PendaftarResponse
import com.example.sipora.data.remote.response.StatisticResponse
import kotlinx.coroutines.flow.Flow

interface SuperAdminRepository {
    fun getAllAdmins(): Flow<ResultState<List<AdminResponse>>>
    fun createAdmin(namaLengkap: String, email: String, password: String): Flow<ResultState<AdminResponse>>
    fun deleteAdmin(adminId: Long): Flow<ResultState<Unit>>
    fun getStatistics(): Flow<ResultState<List<StatisticResponse>>>
    fun getAllPendaftar(): Flow<ResultState<List<PendaftarResponse>>>
}
