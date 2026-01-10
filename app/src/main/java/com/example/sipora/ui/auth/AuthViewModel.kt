package com.example.sipora.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.domain.usecase.LoginUseCase
import com.example.sipora.domain.usecase.LogoutUseCase
import com.example.sipora.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow(AuthUiState())
    val loginState: StateFlow<AuthUiState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(AuthUiState())
    val registerState: StateFlow<AuthUiState> = _registerState.asStateFlow()

    // --- Login Functions ---
    fun onLoginEmailChange(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun onLoginPasswordChange(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun submitLogin() {
        viewModelScope.launch {
            loginUseCase(
                email = _loginState.value.email,
                password = _loginState.value.password
            ).collect { result ->
                when (result) {
                    is ResultState.Loading -> _loginState.update { state -> state.copy(isLoading = true) }
                    is ResultState.Success -> {
                        sessionManager.saveAuth(
                            token = result.data.token, 
                            type = result.data.type, 
                            role = result.data.peran, 
                            divisiId = null, 
                            ormawaId = null
                        )
                        _loginState.update { state ->
                            state.copy(
                                isLoading = false,
                                successName = result.data.namaLengkap,
                                successRole = result.data.peran
                            )
                        }
                    }
                    is ResultState.Error -> _loginState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    // --- Register Functions ---
    fun onRegisterNamaChange(nama: String) {
        _registerState.update { it.copy(namaLengkap = nama) }
    }

    fun onRegisterEmailChange(email: String) {
        _registerState.update { it.copy(email = email) }
    }

    fun onRegisterPasswordChange(password: String) {
        _registerState.update { it.copy(password = password) }
    }

    fun submitRegister() {
        viewModelScope.launch {
            registerUseCase(
                namaLengkap = _registerState.value.namaLengkap,
                email = _registerState.value.email,
                password = _registerState.value.password
            ).collect { result ->
                when (result) {
                    is ResultState.Loading -> _registerState.update { state -> state.copy(isLoading = true) }
                    is ResultState.Success -> {
                        sessionManager.saveAuth(
                            token = result.data.token, 
                            type = result.data.type, 
                            role = result.data.peran, 
                            divisiId = null, 
                            ormawaId = null
                        ) 
                        _registerState.update { state ->
                            state.copy(
                                isLoading = false,
                                successName = result.data.namaLengkap
                            )
                        }
                    }
                    is ResultState.Error -> _registerState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
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
