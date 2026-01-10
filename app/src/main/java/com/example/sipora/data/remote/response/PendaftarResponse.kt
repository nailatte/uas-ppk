package com.example.sipora.data.remote.response

// Represents a single applicant
data class PendaftarResponse(
    val id: Long,
    val namaLengkap: String,
    val ormawa: String, // e.g., "BEM" or "DPM"
    val statusAdmin: String, // e.g., "LOLOS", "GAGAL", "PENDING"
    val statusInterview: String // e.g., "LOLOS", "GAGAL", "PENDING"
)
