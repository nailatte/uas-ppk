package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

// A simplified representation for the UI
data class HasilSeleksiResponse(
    @SerializedName("administrasi") val administrasi: SeleksiItem?,
    @SerializedName("wawancara") val wawancara: SeleksiItem?,
    @SerializedName("akhir") val akhir: StatusAkhir?
)

data class SeleksiItem(
    @SerializedName("lulus") val lulus: Boolean,
    @SerializedName("catatan") val catatan: String?,
    @SerializedName("skor") val skor: Int?
)

data class StatusAkhir(
    @SerializedName("status") val status: String
)
