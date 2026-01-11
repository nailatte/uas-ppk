package com.example.sipora.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.core.util.PendaftaranStatusUtil
import com.example.sipora.data.remote.response.PendaftaranResponse
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.usecase.GetMeUseCase
import com.example.sipora.domain.usecase.GetPendaftarDivisiUseCase
import com.example.sipora.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDashboardSummary(
    val total: Int = 0,
    val pending: Int = 0,
    val lolos: Int = 0,
    val gagal: Int = 0
)

data class AdminDashboardUiState(
    val isLoading: Boolean = false,
    val user: UserResponse? = null,
    val pendaftarList: List<PendaftaranResponse> = emptyList(),
    val summary: AdminDashboardSummary = AdminDashboardSummary(),
    val errorMessage: String? = null
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val getMeUseCase: GetMeUseCase,
    private val getPendaftarDivisiUseCase: GetPendaftarDivisiUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getMeUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> {
                        val user = result.data
                        _uiState.update { it.copy(isLoading = false, user = user, errorMessage = null) }
                        loadPendaftar(user)
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun refresh() {
        loadInitialData()
    }

    fun loadPendaftar(user: UserResponse? = _uiState.value.user) {
        val ormawaId = user?.ormawaId
        val divisiId = user?.divisiId
        if (ormawaId == null || divisiId == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Data admin belum lengkap.") }
            return
        }
        viewModelScope.launch {
            getPendaftarDivisiUseCase(ormawaId, divisiId).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> {
                        val summary = buildSummary(result.data)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                pendaftarList = result.data,
                                summary = summary,
                                errorMessage = null
                            )
                        }
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    private fun buildSummary(list: List<PendaftaranResponse>): AdminDashboardSummary {
        val pending = list.count { PendaftaranStatusUtil.isPendingAdministrasi(it.status) }
        val lolos = list.count { PendaftaranStatusUtil.isLolosAdministrasi(it.status) }
        val gagal = list.count { PendaftaranStatusUtil.isGagalAdministrasi(it.status) }
        return AdminDashboardSummary(
            total = list.size,
            pending = pending,
            lolos = lolos,
            gagal = gagal
        )
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
