package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

data class PendaftarDetailResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("namaLengkap") val namaLengkap: String,
    @SerializedName("email") val email: String,
    @SerializedName("divisiId") val divisiId: Long,
    @SerializedName("divisiNama") val divisiNama: String,
    @SerializedName("dokumenUrl") val dokumenUrl: String?,
    @SerializedName("statusAdministrasi") val statusAdministrasi: String,
    // Add other fields as needed
)
