package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

data class HasilSeleksiDivisiResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("divisiId") val divisiId: Long? = null,
    @SerializedName("namaDivisi") val namaDivisi: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("catatan") val catatan: String? = null,
    @SerializedName("jawabanWawancaraFileName") val jawabanWawancaraFileName: String? = null,
    @SerializedName("jawabanWawancaraPath") val jawabanWawancaraPath: String? = null
)
