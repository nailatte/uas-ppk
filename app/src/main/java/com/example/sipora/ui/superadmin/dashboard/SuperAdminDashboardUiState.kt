package com.example.sipora.ui.superadmin.dashboard

import com.example.sipora.data.remote.response.PendaftarResponse

data class SuperAdminDashboardUiState(
    val isLoading: Boolean = false,
    val ormawaName: String = "",
    val statistics: Map<String, List<PendaftarResponse>> = emptyMap(),
    val errorMessage: String? = null
)
