package com.example.sipora.ui.superadmin.wawancara

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminWawancaraScreen(
    viewModel: SuperAdminWawancaraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.clearMessages()
        }
        uiState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Kelola Jadwal Wawancara") }) }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            PeriodeTabContent(uiState, viewModel, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
private fun PeriodeTabContent(
    uiState: WawancaraUiState,
    viewModel: SuperAdminWawancaraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? -> viewModel.selectPdf(uri) }
    )

    Column(modifier.padding(16.dp)) {
        Text("Form Periode Wawancara", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = uiState.formNamaKegiatan,
                    onValueChange = viewModel::onNamaKegiatanChange,
                    label = { Text("Nama Kegiatan") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.formDeskripsi,
                    onValueChange = viewModel::onDeskripsiChange,
                    label = { Text("Detail (Opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.formTanggalMulai,
                    onValueChange = viewModel::onTanggalMulaiChange,
                    label = { Text("Tanggal Mulai (ISO)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.formTanggalSelesai,
                    onValueChange = viewModel::onTanggalSelesaiChange,
                    label = { Text("Tanggal Selesai (ISO)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val id = uiState.editingPeriodeId
                            if (id == null) {
                                viewModel.createPeriode(uiState.formTanggalMulai, uiState.formTanggalSelesai)
                            } else {
                                viewModel.updatePeriode(id, uiState.formTanggalMulai, uiState.formTanggalSelesai)
                            }
                        },
                        enabled = !uiState.isLoading &&
                            uiState.formNamaKegiatan.isNotBlank() &&
                            uiState.formTanggalMulai.isNotBlank() &&
                            uiState.formTanggalSelesai.isNotBlank()
                    ) {
                        Text(if (uiState.editingPeriodeId == null) "Simpan Periode" else "Update Periode")
                    }
                    if (uiState.editingPeriodeId != null) {
                        OutlinedButton(onClick = viewModel::clearForm, enabled = !uiState.isLoading) {
                            Text("Batal Edit")
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        uiState.activePeriode?.let {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Periode Aktif", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    it.namaKegiatan?.let { nama -> Text(nama, fontWeight = FontWeight.SemiBold) }
                    it.deskripsi?.takeIf { desc -> desc.isNotBlank() }?.let { desc -> Text(desc) }
                    Text("Mulai: ${it.tanggalMulai}")
                    Text("Selesai: ${it.tanggalSelesai}")
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Riwayat Periode", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.periodeList) { periode ->
                Card(Modifier.fillMaxWidth().clickable { viewModel.startEdit(periode) }) {
                    Column(Modifier.padding(16.dp)) {
                        periode.namaKegiatan?.let { nama ->
                            Text(nama, fontWeight = FontWeight.SemiBold)
                        }
                        periode.deskripsi?.takeIf { desc -> desc.isNotBlank() }?.let { desc ->
                            Text(desc)
                        }
                        Text("Mulai: ${periode.tanggalMulai}")
                        Text("Selesai: ${periode.tanggalSelesai}")
                        if (periode.aktif) Badge { Text("Aktif") }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Upload Jadwal Wawancara (PDF)", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
                if (uiState.activePeriode == null) {
                    Text("Belum ada periode wawancara aktif. Buat periode terlebih dahulu.")
                } else {
                    Text("Periode Aktif: ${uiState.activePeriode.tanggalMulai} - ${uiState.activePeriode.tanggalSelesai}")
                    uiState.activePeriode.jadwalWawancaraFileName?.let {
                        Spacer(Modifier.height(8.dp))
                        Text("File Terunggah: $it")
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) }) {
                        Text("Pilih File Jadwal (PDF)")
                    }
                    uiState.selectedPdfUri?.let { uri ->
                        Spacer(Modifier.height(8.dp))
                        Text("File dipilih: ${uri.lastPathSegment ?: uri.path}")
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.uploadJadwal(context) },
                            enabled = !uiState.isLoading
                        ) {
                            Text("Upload Jadwal")
                        }
                    }
                }
            }
        }
    }
}
