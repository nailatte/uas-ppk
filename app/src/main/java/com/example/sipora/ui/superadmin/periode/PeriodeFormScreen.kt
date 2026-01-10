package com.example.sipora.ui.superadmin.periode

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodeFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: PeriodeViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? -> viewModel.onPdfSelected(uri) }
    )

    LaunchedEffect(formState.isSuccess, formState.errorMessage) {
        formState.successMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearMessages()
                onNavigateBack()
            }
        }
        formState.errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearMessages()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Form Periode Pendaftaran") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (formState.isLoading) {
                CircularProgressIndicator()
            }

            OutlinedTextField(
                value = formState.namaKegiatan,
                onValueChange = viewModel::onNamaKegiatanChange,
                label = { Text("Nama Kegiatan") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = formState.deskripsi,
                onValueChange = viewModel::onDeskripsiChange,
                label = { Text("Deskripsi (Opsional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = formState.tanggalMulai,
                onValueChange = viewModel::onTanggalMulaiChange,
                label = { Text("Tanggal Mulai (ISO Format)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = formState.tanggalSelesai,
                onValueChange = viewModel::onTanggalSelesaiChange,
                label = { Text("Tanggal Selesai (ISO Format)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) }) {
                    Text("Pilih File PDF")
                }
                Spacer(Modifier.width(8.dp))
                formState.selectedPdfName?.let {
                    Text(it, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.onPdfSelected(null) }) {
                        Icon(Icons.Default.Clear, contentDescription = "Hapus File")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.submitForm(context) },
                enabled = !formState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}
