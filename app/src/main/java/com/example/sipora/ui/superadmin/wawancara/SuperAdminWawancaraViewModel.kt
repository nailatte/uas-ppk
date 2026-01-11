package com.example.sipora.ui.superadmin.wawancara

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.core.util.RoleUtil
import com.example.sipora.core.util.FileUploadUtil
import com.example.sipora.data.remote.response.PeriodeWawancaraResponse
import com.example.sipora.data.remote.response.UserResponse
import com.example.sipora.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WawancaraUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val currentUser: UserResponse? = null,
    val ormawaId: Long? = null,
    val activePeriode: PeriodeWawancaraResponse? = null,
    val periodeList: List<PeriodeWawancaraResponse> = emptyList(),
    val selectedPdfUri: Uri? = null,
    val selectedPdfName: String? = null,
    val formNamaKegiatan: String = "",
    val formDeskripsi: String = "",
    val formTanggalMulai: String = "",
    val formTanggalSelesai: String = "",
    val editingPeriodeId: Long? = null
)

@HiltViewModel
class SuperAdminWawancaraViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getActiveUseCase: GetPeriodeWawancaraActiveUseCase,
    private val getListUseCase: GetPeriodeWawancaraListUseCase,
    private val createUseCase: CreatePeriodeWawancaraUseCase,
    private val updateUseCase: UpdatePeriodeWawancaraUseCase,
    private val uploadUseCase: UploadJadwalWawancaraUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WawancaraUiState())
    val uiState: StateFlow<WawancaraUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> {
                        val user = result.data
                        val resolvedOrmawaId = user.ormawaId ?: RoleUtil.ormawaIdFromRole(user.peran)
                        _uiState.update { it.copy(isLoading = false, currentUser = user, ormawaId = resolvedOrmawaId) }
                        resolvedOrmawaId?.let { id ->
                            loadPeriodeData(id)
                        }
                    }
                    is ResultState.Error -> _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    private fun loadPeriodeData(ormawaId: Long) {
        viewModelScope.launch {
            getActiveUseCase(ormawaId).collect { result ->
                if (result is ResultState.Success) {
                    _uiState.update { it.copy(activePeriode = result.data) }
                }
            }
        }
        viewModelScope.launch {
            getListUseCase(ormawaId).collect { result ->
                _uiState.update {
                    when (result) {
                        is ResultState.Loading -> it.copy(isLoading = true)
                        is ResultState.Success -> it.copy(isLoading = false, periodeList = result.data)
                        is ResultState.Error -> it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
            }
        }
    }

    fun createPeriode(tanggalMulai: String, tanggalSelesai: String) {
        viewModelScope.launch {
            val state = uiState.value
            createUseCase(
                namaKegiatan = state.formNamaKegiatan,
                deskripsi = state.formDeskripsi.ifBlank { null },
                tanggalMulaiIso = tanggalMulai,
                tanggalSelesaiIso = tanggalSelesai,
                filePart = null
            ).collect { handleResult(it, "Periode berhasil dibuat.") }
        }
    }

    fun updatePeriode(id: Long, tanggalMulai: String?, tanggalSelesai: String?) {
        viewModelScope.launch {
            val state = uiState.value
            updateUseCase(
                id = id,
                namaKegiatan = state.formNamaKegiatan.ifBlank { null },
                deskripsi = state.formDeskripsi.ifBlank { null },
                tanggalMulaiIso = tanggalMulai,
                tanggalSelesaiIso = tanggalSelesai,
                filePart = null
            ).collect { handleResult(it, "Periode berhasil diperbarui.") }
        }
    }

    fun selectPdf(uri: Uri?) {
        _uiState.update { it.copy(selectedPdfUri = uri) }
    }

    fun uploadJadwal(context: Context) {
        val activeId = uiState.value.activePeriode?.id ?: return
        val pdfUri = uiState.value.selectedPdfUri ?: return
        val filePart = FileUploadUtil.createMultipartBody(context, pdfUri, "file") ?: return

        viewModelScope.launch {
            uploadUseCase(activeId, filePart).collect { handleResult(it, "Jadwal berhasil diunggah.") }
        }
    }

    private fun handleResult(result: ResultState<out Any>, successMsg: String) {
        _uiState.update {
            when (result) {
                is ResultState.Loading -> it.copy(isLoading = true, successMessage = null, errorMessage = null)
                is ResultState.Success -> {
                    _uiState.value.ormawaId?.let { id -> loadPeriodeData(id) }
                    it.copy(
                        isLoading = false,
                        successMessage = successMsg,
                        formNamaKegiatan = "",
                        formDeskripsi = "",
                        formTanggalMulai = "",
                        formTanggalSelesai = "",
                        editingPeriodeId = null,
                        selectedPdfUri = null,
                        selectedPdfName = null
                    )
                }
                is ResultState.Error -> it.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    fun onNamaKegiatanChange(value: String) {
        _uiState.update { it.copy(formNamaKegiatan = value) }
    }

    fun onDeskripsiChange(value: String) {
        _uiState.update { it.copy(formDeskripsi = value) }
    }

    fun onTanggalMulaiChange(value: String) {
        _uiState.update { it.copy(formTanggalMulai = value) }
    }

    fun onTanggalSelesaiChange(value: String) {
        _uiState.update { it.copy(formTanggalSelesai = value) }
    }

    fun startEdit(periode: PeriodeWawancaraResponse) {
        _uiState.update {
            it.copy(
                formNamaKegiatan = periode.namaKegiatan.orEmpty(),
                formDeskripsi = periode.deskripsi.orEmpty(),
                formTanggalMulai = periode.tanggalMulai,
                formTanggalSelesai = periode.tanggalSelesai,
                editingPeriodeId = periode.id
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                formNamaKegiatan = "",
                formDeskripsi = "",
                formTanggalMulai = "",
                formTanggalSelesai = "",
                editingPeriodeId = null
            )
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}
