package com.example.sipora.data.remote.request

import com.google.gson.annotations.SerializedName

data class JadwalWawancaraRequest(
    @SerializedName("tanggalWawancara") val tanggalWawancara: String,
    @SerializedName("lokasi") val lokasi: String
)
