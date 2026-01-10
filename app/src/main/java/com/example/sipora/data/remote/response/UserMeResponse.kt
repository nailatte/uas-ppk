package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserMeResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("namaLengkap") val namaLengkap: String,
    @SerializedName("email") val email: String,
    @SerializedName("peran") val peran: String,
    @SerializedName("divisiId") val divisiId: Long?,
    @SerializedName("ormawaId") val ormawaId: Long?
)
