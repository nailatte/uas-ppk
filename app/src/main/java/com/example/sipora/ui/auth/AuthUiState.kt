package com.example.sipora.ui.auth

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val namaLengkap: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successName: String? = null,
    val successRole: String? = null
)
