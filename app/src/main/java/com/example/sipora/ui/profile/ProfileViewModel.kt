package com.example.sipora.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.domain.usecase.ChangePasswordUseCase
import com.example.sipora.domain.usecase.GetProfileUseCase
import com.example.sipora.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val namaLengkap: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            getProfileUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> _uiState.update {
                        it.copy(isLoading = false, namaLengkap = result.data.namaLengkap)
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            updateProfileUseCase(uiState.value.namaLengkap).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
                    is ResultState.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = "Profile updated successfully!") }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun changePassword() {
        if (uiState.value.newPassword != uiState.value.confirmNewPassword) {
            _uiState.update { it.copy(errorMessage = "New passwords do not match.") }
            return
        }
        viewModelScope.launch {
            changePasswordUseCase(uiState.value.currentPassword, uiState.value.newPassword).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
                    is ResultState.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = "Password changed successfully!") }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun onNamaLengkapChange(name: String) {
        _uiState.update { it.copy(namaLengkap = name) }
    }

    fun onCurrentPasswordChange(password: String) {
        _uiState.update { it.copy(currentPassword = password) }
    }

    fun onNewPasswordChange(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }

    fun onConfirmNewPasswordChange(password: String) {
        _uiState.update { it.copy(confirmNewPassword = password) }
    }
    
    fun clearMessages() {
        _uiState.update { it.copy(isSuccess = false, successMessage = null, errorMessage = null) }
    }
}
