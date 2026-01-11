package com.example.sipora.data.remote.api

import com.example.sipora.data.remote.request.AdministrasiRequest
import com.example.sipora.data.remote.request.JadwalWawancaraRequest
import com.example.sipora.data.remote.request.WawancaraRequest
import com.example.sipora.data.remote.response.HasilSeleksiDivisiResponse
import com.example.sipora.data.remote.response.PendaftaranResponse
import com.example.sipora.data.remote.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Part
import retrofit2.http.Query

interface AdminService {

    @GET("users/me")
    suspend fun getMe(): Response<UserResponse>

    @GET("pendaftaran/ormawa/{ormawaId}")
    suspend fun getPendaftarByOrmawa(@Path("ormawaId") ormawaId: Long): Response<List<PendaftaranResponse>>

    @GET("pendaftaran/{id}")
    suspend fun getPendaftaranDetail(@Path("id") id: Long): Response<PendaftaranResponse>

    @POST("seleksi/administrasi/{pendaftaranId}")
    suspend fun submitAdministrasi(
        @Path("pendaftaranId") pendaftaranId: Long,
        @Body request: AdministrasiRequest
    ): Response<Unit> // Assuming simple success response

    @POST("seleksi/wawancara/{pendaftaranId}")
    suspend fun submitWawancara(
        @Path("pendaftaranId") pendaftaranId: Long,
        @Body request: WawancaraRequest
    ): Response<Unit>

    @POST("seleksi/wawancara/{pendaftaranId}/jadwal")
    suspend fun setJadwalWawancara(
        @Path("pendaftaranId") pendaftaranId: Long,
        @Body request: JadwalWawancaraRequest
    ): Response<Unit>

    @GET("seleksi/hasil/{pendaftaranId}")
    suspend fun getHasilSeleksi(@Path("pendaftaranId") pendaftaranId: Long): Response<List<HasilSeleksiDivisiResponse>>

    @Multipart
    @POST("seleksi/wawancara/{pendaftaranId}/jawaban")
    suspend fun uploadJawabanWawancara(
        @Path("pendaftaranId") pendaftaranId: Long,
        @Query("divisiId") divisiId: Long,
        @Part file: MultipartBody.Part
    ): Response<HasilSeleksiDivisiResponse>
}
