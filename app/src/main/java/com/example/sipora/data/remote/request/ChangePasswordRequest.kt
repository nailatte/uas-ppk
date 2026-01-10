package com.example.sipora.data.remote.request

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
