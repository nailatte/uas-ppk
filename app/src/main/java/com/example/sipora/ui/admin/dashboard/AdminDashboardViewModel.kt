package com.example.sipora.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.core.util.RoleUtil
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.domain.usecase.GetMeUseCase
import com.example.sipora.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val errorMessage: String? = null
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val getMeUseCase: GetMeUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionManager: SessionManager
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
                        val ormawaId = user.ormawaId ?: RoleUtil.ormawaIdFromRole(user.peran)
                        sessionManager.saveAuth(
                            token = sessionManager.tokenFlow.first() ?: "",
                            type = sessionManager.tokenTypeFlow.first() ?: "",
                            role = user.peran,
                            divisiId = user.divisiId,
                            ormawaId = ormawaId
                        )
                        _uiState.update { it.copy(isLoading = false, userName = user.namaLengkap) }
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
