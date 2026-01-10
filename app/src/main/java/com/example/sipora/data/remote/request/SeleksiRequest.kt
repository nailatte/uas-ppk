package com.example.sipora.data.remote.request

import com.google.gson.annotations.SerializedName

data class AdministrasiRequest(
    @SerializedName("lulus") val lulus: Boolean,
    @SerializedName("catatan") val catatan: String?,
    @SerializedName("skor") val skor: Int?
)

data class WawancaraRequest(
    @SerializedName("nilai") val nilai: Int,
    @SerializedName("catatan") val catatan: String?
)
