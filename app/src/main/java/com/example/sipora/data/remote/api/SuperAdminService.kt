package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.response.AdminResponse
import com.example.sipora.data.remote.response.PendaftarResponse
import com.example.sipora.data.remote.response.StatisticResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SuperAdminService {

    @GET("admin/admins")
    suspend fun getAllAdmins(): Response<List<AdminResponse>>

    @POST("admin/create-admin")
    suspend fun createAdmin(
        @Query("namaLengkap") namaLengkap: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<AdminResponse>

    @DELETE("admin/admins/{adminId}")
    suspend fun deleteAdmin(@Path("adminId") adminId: Long): Response<Unit>

    @GET("admin/statistics")
    suspend fun getStatistics(): Response<List<StatisticResponse>>

    // Placeholder for getting all applicants
    @GET("admin/pendaftar")
    suspend fun getAllPendaftar(): Response<List<PendaftarResponse>>
}
