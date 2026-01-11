package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.data.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PeriodeWawancaraService {

    @GET("users/me")
    suspend fun getMe(): Response<UserResponse>

    @Multipart
    @POST("periode-wawancara")
    suspend fun createPeriode(
        @Query("namaKegiatan") namaKegiatan: String,
        @Query("deskripsi") deskripsi: String?,
        @Query("tanggalMulai") tanggalMulai: String,
        @Query("tanggalSelesai") tanggalSelesai: String,
        @Part file: MultipartBody.Part?
    ): Response<PeriodeWawancaraResponse>

    @Multipart
    @PUT("periode-wawancara/{id}")
    suspend fun updatePeriode(
        @Path("id") id: Long,
        @Query("namaKegiatan") namaKegiatan: String?,
        @Query("deskripsi") deskripsi: String?,
        @Query("tanggalMulai") tanggalMulai: String?,
        @Query("tanggalSelesai") tanggalSelesai: String?,
        @Part file: MultipartBody.Part?
    ): Response<PeriodeWawancaraResponse>

    @GET("periode-wawancara/ormawa/{ormawaId}")
    suspend fun getListByOrmawa(@Path("ormawaId") ormawaId: Long): Response<List<PeriodeWawancaraResponse>>

    @GET("periode-wawancara/ormawa/{ormawaId}/active")
    suspend fun getActiveByOrmawa(@Path("ormawaId") ormawaId: Long): Response<PeriodeWawancaraResponse>

    @Multipart
    @POST("periode-wawancara/{id}/jadwal")
    suspend fun uploadJadwal(
        @Path("id") id: Long,
        @Part file: MultipartBody.Part
    ): Response<PeriodeWawancaraResponse>
}
