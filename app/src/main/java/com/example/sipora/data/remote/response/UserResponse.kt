package com.example.sipora.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("namaLengkap") val namaLengkap: String,
    @SerializedName("email") val email: String,
    @SerializedName("peran") val peran: String,
    @SerializedName("divisiId") val divisiId: Long?,
    @SerializedName("namaDivisi") val namaDivisi: String?,
    @SerializedName("ormawaId") val ormawaId: Long?,
    @SerializedName("namaOrmawa") val namaOrmawa: String?
)
