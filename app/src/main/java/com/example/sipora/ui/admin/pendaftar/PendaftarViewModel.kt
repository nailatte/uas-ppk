package com.example.sipora.ui.admin.pendaftar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.data.remote.response.PendaftarItemResponse
import com.example.sipora.domain.usecase.GetPendaftarByOrmawaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PendaftarListUiState(
    val isLoading: Boolean = false,
    val pendaftarList: List<PendaftarItemResponse> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class PendaftarViewModel @Inject constructor(
    private val getPendaftarByOrmawaUseCase: GetPendaftarByOrmawaUseCase,
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendaftarListUiState())
    val uiState: StateFlow<PendaftarListUiState> = _uiState.asStateFlow()

    init {
        loadPendaftar()
    }

    fun loadPendaftar() {
        viewModelScope.launch {
            val ormawaId = sessionManager.ormawaIdFlow.first() ?: return@launch
            val divisiId = sessionManager.divisiIdFlow.first() ?: return@launch

            getPendaftarByOrmawaUseCase(ormawaId).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> {
                        val filteredList = result.data.filter { it.divisiId == divisiId }
                        _uiState.update { it.copy(isLoading = false, pendaftarList = filteredList) }
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }
}
