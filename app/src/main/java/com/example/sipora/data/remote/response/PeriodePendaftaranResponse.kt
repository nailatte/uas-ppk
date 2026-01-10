package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

data class PeriodePendaftaranResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("ormawaId") val ormawaId: Long,
    @SerializedName("namaOrmawa") val namaOrmawa: String,
    @SerializedName("namaKegiatan") val namaKegiatan: String,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("tanggalMulai") val tanggalMulai: String,
    @SerializedName("tanggalSelesai") val tanggalSelesai: String,
    @SerializedName("infoFileName") val infoFileName: String?,
    @SerializedName("infoFilePath") val infoFilePath: String?,
    @SerializedName("aktif") val aktif: Boolean
)
