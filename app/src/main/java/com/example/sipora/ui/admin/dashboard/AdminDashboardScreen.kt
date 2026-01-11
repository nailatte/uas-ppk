package com.example.sipora.ui.admin.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.sipora.core.util.PendaftaranStatusUtil
import com.example.sipora.data.remote.response.PendaftaranResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToAdministrasi: () -> Unit,
    onNavigateToWawancara: () -> Unit,
    onNavigateToKelolaWawancara: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedStat by remember { mutableStateOf<StatDialogData?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    val errorMessage = uiState.errorMessage
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(errorMessage ?: "Terjadi kesalahan.", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = viewModel::refresh) {
                            Text("Coba Lagi")
                        }
                    }
                }
                else -> {
                    AdminHeaderSection(
                        nameOrmawa = uiState.user?.namaOrmawa ?: "-",
                        nameDivisi = uiState.user?.namaDivisi ?: "-"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AdminSummarySection(
                        total = uiState.summary.total,
                        pending = uiState.summary.pending,
                        lolos = uiState.summary.lolos,
                        gagal = uiState.summary.gagal,
                        onCardClick = { stat ->
                            val list = filterPendaftar(uiState.pendaftarList, stat)
                            selectedStat = StatDialogData(title = stat.label, items = list)
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onNavigateToAdministrasi,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kelola Administrasi")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onNavigateToWawancara,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Penilaian Wawancara")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onNavigateToKelolaWawancara,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kelola Wawancara")
                    }
                }
            }
        }
    }

    if (selectedStat != null) {
        StatListDialog(
            title = selectedStat?.title.orEmpty(),
            items = selectedStat?.items.orEmpty(),
            onDismiss = { selectedStat = null }
        )
    }
}

@Composable
private fun AdminHeaderSection(nameOrmawa: String, nameDivisi: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Admin Dashboard", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Ormawa: $nameOrmawa", style = MaterialTheme.typography.bodyMedium)
            Text("Divisi: $nameDivisi", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun AdminSummarySection(
    total: Int,
    pending: Int,
    lolos: Int,
    gagal: Int,
    onCardClick: (AdminStatType) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        SummaryCard(title = "Total Pendaftar", value = total) {
            onCardClick(AdminStatType.TOTAL)
        }
        SummaryCard(title = "Pending", value = pending) {
            onCardClick(AdminStatType.PENDING)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        SummaryCard(title = "Lolos Admin", value = lolos) {
            onCardClick(AdminStatType.LOLOS)
        }
        SummaryCard(title = "Tidak Lolos", value = gagal) {
            onCardClick(AdminStatType.GAGAL)
        }
    }
}

@Composable
private fun RowScope.SummaryCard(title: String, value: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(value.toString(), style = MaterialTheme.typography.headlineSmall)
        }
    }
}

private enum class AdminStatType(val label: String) {
    TOTAL("Total Pendaftar"),
    PENDING("Pending Administrasi"),
    LOLOS("Lolos Administrasi"),
    GAGAL("Tidak Lolos Administrasi")
}

private data class StatDialogData(
    val title: String,
    val items: List<PendaftaranResponse>
)

private fun filterPendaftar(
    items: List<PendaftaranResponse>,
    type: AdminStatType
): List<PendaftaranResponse> {
    return when (type) {
        AdminStatType.TOTAL -> items
        AdminStatType.PENDING -> items.filter { PendaftaranStatusUtil.isPendingAdministrasi(it.status) }
        AdminStatType.LOLOS -> items.filter { PendaftaranStatusUtil.isLolosAdministrasi(it.status) }
        AdminStatType.GAGAL -> items.filter { PendaftaranStatusUtil.isGagalAdministrasi(it.status) }
    }
}

@Composable
private fun StatListDialog(
    title: String,
    items: List<PendaftaranResponse>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            if (items.isEmpty()) {
                Text("Belum ada pendaftar pada kategori ini.")
            } else {
                LazyColumn {
                    items(items, key = { it.id }) { pendaftar ->
                        Text(pendaftar.namaLengkap, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tutup") }
        }
    )
}
