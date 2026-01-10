package com.example.sipora.ui.superadmin.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.domain.usecase.CreateAdminUseCase
import com.example.sipora.domain.usecase.DeleteAdminUseCase
import com.example.sipora.domain.usecase.GetAdminsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getAdminsUseCase: GetAdminsUseCase,
    private val createAdminUseCase: CreateAdminUseCase,
    private val deleteAdminUseCase: DeleteAdminUseCase,
    private val sessionManager: SessionManager // Injected SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        loadAdmins()
    }

    fun loadAdmins() {
        viewModelScope.launch {
            val role = sessionManager.roleFlow.first() ?: return@launch
            val ormawa = if (role.contains("bem", ignoreCase = true)) "bem" else "dpm"

            getAdminsUseCase().onEach { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is ResultState.Success -> {
                        // Filter the admin list based on the superadmin's ormawa
                        val filteredAdmins = result.data.filter { admin ->
                            admin.peran.contains(ormawa, ignoreCase = true)
                        }
                        _uiState.update { it.copy(isLoading = false, admins = filteredAdmins) }
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun createAdmin() {
        viewModelScope.launch {
            createAdminUseCase(
                namaLengkap = _uiState.value.namaLengkap,
                email = _uiState.value.email,
                password = _uiState.value.password
            ).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
                    is ResultState.Success -> {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                        loadAdmins() // Refresh the list after successful creation
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun deleteAdmin(adminId: Long) {
        viewModelScope.launch {
            deleteAdminUseCase(adminId).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is ResultState.Success -> loadAdmins() // Refresh the list on success
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun onNamaLengkapChange(nama: String) {
        _uiState.update { it.copy(namaLengkap = nama) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun resetSuccessFlag() {
        _uiState.update { it.copy(isSuccess = false, errorMessage = null) }
    }
}
