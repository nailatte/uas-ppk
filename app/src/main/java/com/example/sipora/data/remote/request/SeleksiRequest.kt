package com.example.sipora.data.remote.request

import com.google.gson.annotations.SerializedName

data class AdministrasiRequest(
    @SerializedName("status") val status: String,
    @SerializedName("catatan") val catatan: String?
)

data class WawancaraRequest(
    @SerializedName("nilai") val nilai: Int,
    @SerializedName("catatan") val catatan: String?
)
