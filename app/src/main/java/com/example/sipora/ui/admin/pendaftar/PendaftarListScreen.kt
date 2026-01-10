package com.example.sipora.ui.admin.pendaftar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sipora.data.remote.response.PendaftarItemResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendaftarListScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: PendaftarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Pendaftar") },
                actions = {
                    IconButton(onClick = { viewModel.loadPendaftar() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.pendaftarList.isEmpty()) {
                Text("Tidak ada pendaftar untuk divisi Anda.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.pendaftarList, key = { it.id }) {
                        PendaftarCard(pendaftar = it, onClick = { onNavigateToDetail(it.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun PendaftarCard(pendaftar: PendaftarItemResponse, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(pendaftar.namaLengkap, style = MaterialTheme.typography.titleMedium)
            Text(pendaftar.email, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge { Text(pendaftar.statusAdministrasi) }
                Badge { Text(pendaftar.statusWawancara) }
                Badge(containerColor = MaterialTheme.colorScheme.primary) { Text(pendaftar.statusAkhir, color = MaterialTheme.colorScheme.onPrimary) }
            }
        }
    }
}
