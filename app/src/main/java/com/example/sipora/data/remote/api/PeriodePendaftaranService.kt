package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.response.PeriodePendaftaranResponse
import com.example.sipora.data.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PeriodePendaftaranService {

    @GET("users/me")
    suspend fun getMe(): Response<UserResponse>

    @GET("periode-pendaftaran/{id}")
    suspend fun getById(@Path("id") id: Long): Response<PeriodePendaftaranResponse>

    @GET("periode-pendaftaran/ormawa/{ormawaId}")
    suspend fun getByOrmawa(@Path("ormawaId") ormawaId: Long): Response<List<PeriodePendaftaranResponse>>

    @GET("periode-pendaftaran/ormawa/{ormawaId}/active")
    suspend fun getActive(@Path("ormawaId") ormawaId: Long): Response<PeriodePendaftaranResponse>

    @Multipart
    @POST("periode-pendaftaran")
    suspend fun create(
        @Query("namaKegiatan") namaKegiatan: String,
        @Query("deskripsi") deskripsi: String?,
        @Query("tanggalMulai") tanggalMulai: String,
        @Query("tanggalSelesai") tanggalSelesai: String,
        @Part file: MultipartBody.Part?
    ): Response<PeriodePendaftaranResponse>

    @Multipart
    @PUT("periode-pendaftaran/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Query("namaKegiatan") namaKegiatan: String?,
        @Query("deskripsi") deskripsi: String?,
        @Query("tanggalMulai") tanggalMulai: String?,
        @Query("tanggalSelesai") tanggalSelesai: String?,
        @Part file: MultipartBody.Part?
    ): Response<PeriodePendaftaranResponse>
}
