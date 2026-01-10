package com.example.sipora.ui.superadmin.periode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sipora.data.remote.response.PeriodePendaftaranResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodeListScreen(
    onNavigateToForm: (id: Long?) -> Unit,
    viewModel: PeriodeViewModel = hiltViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadListData()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToForm(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Buat Periode")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (listState.isLoading) {
                CircularProgressIndicator()
            } else if (listState.activePeriode == null && listState.periodeList.isEmpty()) {
                Text(
                    text = "Belum ada periode pendaftaran. Tekan tombol (+) untuk membuat.",
                    modifier = Modifier.padding(32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    listState.activePeriode?.let {
                        ActivePeriodeCard(it)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text("Riwayat Periode", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(listState.periodeList, key = { it.id }) {
                            PeriodeItemCard(periode = it, onClick = { onNavigateToForm(it.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivePeriodeCard(periode: PeriodePendaftaranResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Periode Aktif", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(periode.namaKegiatan, fontWeight = FontWeight.Bold)
            Text("${periode.tanggalMulai} - ${periode.tanggalSelesai}")
        }
    }
}

@Composable
fun PeriodeItemCard(periode: PeriodePendaftaranResponse, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(periode.namaKegiatan, fontWeight = FontWeight.Bold)
                Text("${periode.tanggalMulai} - ${periode.tanggalSelesai}")
            }
            if (periode.aktif) {
                Badge { Text("Aktif") }
            }
        }
    }
}
