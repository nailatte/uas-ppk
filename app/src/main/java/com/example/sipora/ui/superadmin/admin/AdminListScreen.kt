package com.example.sipora.ui.superadmin.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminListScreen(
    onNavigateToCreateAdmin: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Admin") },
                actions = {
                    IconButton(onClick = { viewModel.loadAdmins() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateAdmin) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Admin")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading && uiState.admins.isEmpty()) {
                CircularProgressIndicator()
            } else if (!uiState.isLoading && uiState.admins.isEmpty()) {
                Text(
                    text = "Belum ada admin untuk ormawa Anda. Tekan tombol (+) untuk menambah.",
                    modifier = Modifier.padding(32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.admins, key = { it.id }) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = it.namaLengkap, style = MaterialTheme.typography.titleMedium)
                                    Text(text = it.email, style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { viewModel.deleteAdmin(it.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus Admin")
                                }
                            }
                        }
                    }
                }
            }

            uiState.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
