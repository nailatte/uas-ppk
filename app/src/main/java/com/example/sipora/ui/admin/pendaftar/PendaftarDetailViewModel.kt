package com.example.sipora.ui.admin.pendaftar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.data.remote.request.AdministrasiRequest
import com.example.sipora.data.remote.request.JadwalWawancaraRequest
import com.example.sipora.data.remote.request.WawancaraRequest
import com.example.sipora.data.remote.response.PendaftarDetailResponse
import com.example.sipora.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PendaftarDetailUiState(
    val isLoading: Boolean = false,
    val pendaftar: PendaftarDetailResponse? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class PendaftarDetailViewModel @Inject constructor(
    private val getPendaftaranDetailUseCase: GetPendaftaranDetailUseCase,
    private val submitAdministrasiUseCase: SubmitAdministrasiUseCase,
    private val setJadwalWawancaraUseCase: SetJadwalWawancaraUseCase,
    private val submitWawancaraUseCase: SubmitWawancaraUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pendaftaranId: Long = checkNotNull(savedStateHandle["id"])
    private val _uiState = MutableStateFlow(PendaftarDetailUiState())
    val uiState: StateFlow<PendaftarDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            getPendaftaranDetailUseCase(pendaftaranId).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> _uiState.update { it.copy(isLoading = false, pendaftar = result.data) }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun submitNilaiAdministrasi(lulus: Boolean, catatan: String?, skor: Int?) {
        val request = AdministrasiRequest(lulus, catatan, skor)
        viewModelScope.launch {
            submitAdministrasiUseCase(pendaftaranId, request).collect { handleActionResult(it, "Nilai administrasi berhasil dikirim.") }
        }
    }

    fun setJadwal(tanggal: String, lokasi: String) {
        val request = JadwalWawancaraRequest(tanggal, lokasi)
        viewModelScope.launch {
            setJadwalWawancaraUseCase(pendaftaranId, request).collect { handleActionResult(it, "Jadwal wawancara berhasil diatur.") }
        }
    }

    fun submitNilaiWawancara(nilai: Int, catatan: String?) {
        val request = WawancaraRequest(nilai, catatan)
        viewModelScope.launch {
            submitWawancaraUseCase(pendaftaranId, request).collect { handleActionResult(it, "Nilai wawancara berhasil dikirim.") }
        }
    }

    private fun handleActionResult(result: ResultState<Unit>, successMsg: String) {
        when (result) {
            is ResultState.Loading -> _uiState.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
            is ResultState.Success -> {
                _uiState.update { it.copy(isLoading = false, successMessage = successMsg) }
                loadDetail() // Refresh detail data
            }
            is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}
