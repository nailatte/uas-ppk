package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

data class PendaftarItemResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("namaLengkap") val namaLengkap: String,
    @SerializedName("email") val email: String,
    @SerializedName("divisiId") val divisiId: Long,
    @SerializedName("divisiNama") val divisiNama: String,
    @SerializedName("statusAdministrasi") val statusAdministrasi: String,
    @SerializedName("statusWawancara") val statusWawancara: String,
    @SerializedName("statusAkhir") val statusAkhir: String
)
