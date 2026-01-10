package com.example.sipora.ui.superadmin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.data.remote.response.PendaftarResponse
import com.example.sipora.domain.usecase.GetAllPendaftarUseCase
import com.example.sipora.domain.usecase.GetAdminsUseCase
import com.example.sipora.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SuperAdminDashboardViewModel @Inject constructor(
    private val getAllPendaftarUseCase: GetAllPendaftarUseCase,
    private val getAdminsUseCase: GetAdminsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SuperAdminDashboardUiState())
    val uiState: StateFlow<SuperAdminDashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.roleFlow.first()?.let {
                loadDashboardData(it)
            }
        }
    }

    private fun loadDashboardData(role: String) {
        val ormawa = if (role.contains("bem", ignoreCase = true)) "BEM" else "DPM"

        getAllPendaftarUseCase().onEach { result ->
            when (result) {
                is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                is ResultState.Success -> {
                    val filteredPendaftar = result.data.filter { it.ormawa.equals(ormawa, ignoreCase = true) }
                    val calculatedStats = calculateStatistics(filteredPendaftar)
                    _uiState.update { it.copy(isLoading = false, ormawaName = ormawa, statistics = calculatedStats) }
                }
                is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }.launchIn(viewModelScope)

    }

    private fun calculateStatistics(pendaftar: List<PendaftarResponse>): Map<String, List<PendaftarResponse>> {
        val lolosAdministrasi = pendaftar.filter { it.statusAdmin == "LOLOS" }
        val menungguPenilaian = pendaftar.filter { it.statusAdmin == "PENDING" }
        val diterima = pendaftar.filter { it.statusInterview == "LOLOS" }
        val ditolak = pendaftar.filter { it.statusAdmin == "GAGAL" || it.statusInterview == "GAGAL" }

        return mapOf(
            "Jumlah Pendaftar" to pendaftar,
            "Lolos Administrasi" to lolosAdministrasi,
            "Menunggu Penilaian" to menungguPenilaian,
            "Diterima" to diterima,
            "Ditolak" to ditolak
        )
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
