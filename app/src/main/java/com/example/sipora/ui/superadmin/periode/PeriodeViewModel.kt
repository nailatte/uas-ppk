package com.example.sipora.ui.superadmin.periode

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sipora.core.result.ResultState
import com.example.sipora.core.util.FileUploadUtil
import com.example.sipora.core.util.RoleUtil
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.data.remote.response.PeriodePendaftaranResponse
import com.example.sipora.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeriodeViewModel @Inject constructor(
    private val getPeriodeByOrmawaUseCase: GetPeriodeByOrmawaUseCase,
    private val getActivePeriodeUseCase: GetActivePeriodeUseCase,
    private val getPeriodeByIdUseCase: GetPeriodeByIdUseCase,
    private val createPeriodeUseCase: CreatePeriodeUseCase,
    private val updatePeriodeUseCase: UpdatePeriodeUseCase,
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _listState = MutableStateFlow(PeriodeListUiState())
    val listState: StateFlow<PeriodeListUiState> = _listState.asStateFlow()

    private val _formState = MutableStateFlow(PeriodeFormUiState())
    val formState: StateFlow<PeriodeFormUiState> = _formState.asStateFlow()

    private var ormawaId: Long = 0
    private val periodeId: Long? = savedStateHandle.get<Long>("id")

    init {
        viewModelScope.launch {
            val role = sessionManager.roleFlow.first() ?: ""
            ormawaId = RoleUtil.ormawaIdFromRole(role)

            if (periodeId != null && periodeId != -1L) {
                loadPeriodeDetails(periodeId)
            } else {
                loadListData()
            }
        }
    }

    fun loadListData() {
        viewModelScope.launch {
            getActivePeriodeUseCase(ormawaId).collect { result ->
                if (result is ResultState.Success) {
                    _listState.update { it.copy(activePeriode = result.data) }
                }
            }
        }
        viewModelScope.launch {
            getPeriodeByOrmawaUseCase(ormawaId).collect { result ->
                when (result) {
                    is ResultState.Loading -> _listState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> _listState.update { it.copy(isLoading = false, periodeList = result.data) }
                    is ResultState.Error -> _listState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    private fun loadPeriodeDetails(id: Long) {
        viewModelScope.launch {
            getPeriodeByIdUseCase(id).collect { result ->
                when (result) {
                    is ResultState.Loading -> _formState.update { it.copy(isLoading = true) }
                    is ResultState.Success -> {
                        val data = result.data
                        _formState.update {
                            it.copy(
                                isLoading = false,
                                namaKegiatan = data.namaKegiatan,
                                deskripsi = data.deskripsi ?: "",
                                tanggalMulai = data.tanggalMulai,
                                tanggalSelesai = data.tanggalSelesai,
                                selectedPdfName = data.infoFileName
                            )
                        }
                    }
                    is ResultState.Error -> _formState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun submitForm(context: Context) {
        if (periodeId != null && periodeId != -1L) {
            submitUpdate(periodeId, context)
        } else {
            submitCreate(context)
        }
    }

    private fun submitCreate(context: Context) {
        viewModelScope.launch {
            val filePart = formState.value.selectedPdfUri?.let { FileUploadUtil.createMultipartBody(context, it, "file") }
            createPeriodeUseCase(
                namaKegiatan = formState.value.namaKegiatan,
                deskripsi = formState.value.deskripsi,
                tanggalMulai = formState.value.tanggalMulai,
                tanggalSelesai = formState.value.tanggalSelesai,
                filePart = filePart
            ).collect { handleFormResult(it) }
        }
    }

    private fun submitUpdate(id: Long, context: Context) {
        viewModelScope.launch {
            val filePart = formState.value.selectedPdfUri?.let { FileUploadUtil.createMultipartBody(context, it, "file") }
            updatePeriodeUseCase(
                id = id,
                namaKegiatan = formState.value.namaKegiatan,
                deskripsi = formState.value.deskripsi,
                tanggalMulai = formState.value.tanggalMulai,
                tanggalSelesai = formState.value.tanggalSelesai,
                filePart = filePart
            ).collect { handleFormResult(it) }
        }
    }

    private fun handleFormResult(result: ResultState<PeriodePendaftaranResponse>) {
        when (result) {
            is ResultState.Loading -> _formState.update { it.copy(isLoading = true, successMessage = null, errorMessage = null) }
            is ResultState.Success -> _formState.update { it.copy(isLoading = false, isSuccess = true, successMessage = "Data berhasil disimpan!") }
            is ResultState.Error -> _formState.update { it.copy(isLoading = false, errorMessage = result.message) }
        }
    }

    fun onNamaKegiatanChange(value: String) = _formState.update { it.copy(namaKegiatan = value) }
    fun onDeskripsiChange(value: String) = _formState.update { it.copy(deskripsi = value) }
    fun onTanggalMulaiChange(value: String) = _formState.update { it.copy(tanggalMulai = value) }
    fun onTanggalSelesaiChange(value: String) = _formState.update { it.copy(tanggalSelesai = value) }
    fun onPdfSelected(uri: Uri?) {
        _formState.update { it.copy(selectedPdfUri = uri, selectedPdfName = if (uri == null) null else "File baru dipilih.") }
    }

    fun clearMessages() {
        _formState.update { it.copy(isSuccess = false, successMessage = null, errorMessage = null) }
        _listState.update { it.copy(errorMessage = null) }
    }
}

data class PeriodeListUiState(
    val isLoading: Boolean = false,
    val periodeList: List<PeriodePendaftaranResponse> = emptyList(),
    val activePeriode: PeriodePendaftaranResponse? = null,
    val errorMessage: String? = null
)

data class PeriodeFormUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    val namaKegiatan: String = "",
    val deskripsi: String = "",
    val tanggalMulai: String = "",
    val tanggalSelesai: String = "",
    val selectedPdfUri: Uri? = null,
    val selectedPdfName: String? = null
)
