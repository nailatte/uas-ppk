package com.example.sipora.ui.admin.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminDashboardScreen(
    onNavigateToPendaftar: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Welcome, Admin", style = MaterialTheme.typography.headlineSmall)
            Text(uiState.userName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onNavigateToPendaftar) {
                Text("Lihat Pendaftar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToProfile) {
                Text("Edit Profile")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                viewModel.logout()
                onLogout()
            }) {
                Text("Logout")
            }
            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
