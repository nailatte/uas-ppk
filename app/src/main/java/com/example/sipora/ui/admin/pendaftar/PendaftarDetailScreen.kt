package com.example.sipora.ui.admin.pendaftar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendaftarDetailScreen(
    viewModel: PendaftarDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showNilaiAdminDialog by remember { mutableStateOf(false) }
    var showJadwalDialog by remember { mutableStateOf(false) }
    var showNilaiWawancaraDialog by remember { mutableStateOf(false) }

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
        topBar = { TopAppBar(title = { Text(uiState.pendaftar?.namaLengkap ?: "Detail Pendaftar") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                uiState.pendaftar?.let { pendaftar ->
                    Text("Nama: ${pendaftar.namaLengkap}")
                    Text("Email: ${pendaftar.email}")
                    Text("Divisi: ${pendaftar.divisiNama}")
                    Text("Status Administrasi: ${pendaftar.statusAdministrasi}")
                    Spacer(Modifier.height(16.dp))
                    // Action Buttons
                    OutlinedButton(onClick = { showNilaiAdminDialog = true }) { Text("Nilai Administrasi") }
                    OutlinedButton(onClick = { showJadwalDialog = true }) { Text("Set Jadwal Wawancara") }
                    OutlinedButton(onClick = { showNilaiWawancaraDialog = true }) { Text("Nilai Wawancara") }
                }
            }
        }
    }

    if (showNilaiAdminDialog) {
        NilaiAdministrasiDialog(
            onDismiss = { showNilaiAdminDialog = false },
            onSubmit = { lulus, catatan, skor ->
                viewModel.submitNilaiAdministrasi(lulus, catatan, skor)
                showNilaiAdminDialog = false
            }
        )
    }
    // Other dialogs would be implemented similarly
}

@Composable
fun NilaiAdministrasiDialog(onDismiss: () -> Unit, onSubmit: (Boolean, String, Int) -> Unit) {
    var lulus by remember { mutableStateOf(true) }
    var catatan by remember { mutableStateOf("") }
    var skor by remember { mutableStateOf("80") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nilai Administrasi") },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = lulus, onClick = { lulus = true })
                    Text("Lulus")
                    RadioButton(selected = !lulus, onClick = { lulus = false })
                    Text("Tidak Lulus")
                }
                OutlinedTextField(value = catatan, onValueChange = { catatan = it }, label = { Text("Catatan") })
                OutlinedTextField(value = skor, onValueChange = { skor = it }, label = { Text("Skor") })
            }
        },
        confirmButton = { Button(onClick = { onSubmit(lulus, catatan, skor.toIntOrNull() ?: 0) }) { Text("Submit") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
