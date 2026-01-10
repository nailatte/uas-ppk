package com.example.sipora.data.remote.response

data class AuthResponse(
    val token: String,
    val type: String,
    val id: Long,
    val namaLengkap: String,
    val email: String,
    val peran: String
)
