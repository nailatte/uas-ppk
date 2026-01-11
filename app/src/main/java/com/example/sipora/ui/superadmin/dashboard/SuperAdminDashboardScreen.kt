package com.example.sipora.ui.superadmin.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sipora.data.remote.response.PendaftarResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminDashboardScreen(
    onLogout: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPeriode: () -> Unit,
    onNavigateToWawancara: () -> Unit,
    viewModel: SuperAdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var selectedStat by remember { mutableStateOf<Pair<String, List<PendaftarResponse>>?>(null) }

    if (showDialog && selectedStat != null) {
        StudentListDialog(
            title = selectedStat!!.first,
            students = selectedStat!!.second,
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Superadmin Dashboard") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Keluar")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Kelola Periode") },
                    label = { Text("Periode") },
                    selected = false,
                    onClick = onNavigateToPeriode
                )
                 NavigationBarItem(
                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Kelola Wawancara") },
                    label = { Text("Wawancara") },
                    selected = false,
                    onClick = onNavigateToWawancara
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Group, contentDescription = "Kelola Admin") },
                    label = { Text("Admin") },
                    selected = false,
                    onClick = onNavigateToAdmin
                )
            }
        }
    ) { paddingValues ->
        StatisticsContent(uiState, paddingValues) {
            selectedStat = it
            showDialog = true
        }
    }
}

@Composable
fun StatisticsContent(
    uiState: SuperAdminDashboardUiState,
    paddingValues: PaddingValues,
    onStatClick: (Pair<String, List<PendaftarResponse>>) -> Unit
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Statistik Pendaftaran ${uiState.ormawaName}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(uiState.statistics.entries.chunked(2)) { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { (label, pendaftarList) ->
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard(label = label, count = pendaftarList.size) {
                            onStatClick(label to pendaftarList)
                        }
                    }
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, count: Int, onClick: () -> Unit) {
    val color = getColorForStat(label)
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 14.sp, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(count.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun StudentListDialog(title: String, students: List<PendaftarResponse>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            if (students.isEmpty()) {
                Text("Tidak ada mahasiswa dalam kategori ini.")
            } else {
                LazyColumn {
                    items(students) { student ->
                        Text(student.namaLengkap, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Composable
fun getColorForStat(label: String): Color {
    return when (label) {
        "Jumlah Pendaftar" -> MaterialTheme.colorScheme.primary
        "Lolos Administrasi" -> Color(0xFF388E3C) // Green
        "Menunggu Penilaian" -> MaterialTheme.colorScheme.secondary
        "Diterima" -> Color(0xFF1976D2) // Blue
        "Ditolak" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
}
