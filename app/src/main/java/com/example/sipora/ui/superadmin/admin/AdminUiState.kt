package com.example.sipora.ui.superadmin.admin

import com.example.sipora.data.remote.response.AdminResponse

data class AdminUiState(
    val admins: List<AdminResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false, // To signal successful creation/deletion
    // Form state
    val namaLengkap: String = "",
    val email: String = "",
    val password: String = ""
)
