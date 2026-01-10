package com.example.sipora.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.isSuccess, uiState.errorMessage) {
        val successMessage = uiState.successMessage
        if (uiState.isSuccess && successMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(successMessage)
                viewModel.clearMessages()
                onNavigateBack()
            }
        }
        val errorMessage = uiState.errorMessage
        if (errorMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.clearMessages()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { 
            TopAppBar(title = { Text("Edit Profile") }) 
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            // Edit Information Section
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Edit Informasi", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.namaLengkap,
                        onValueChange = viewModel::onNamaLengkapChange,
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.updateProfile() }, modifier = Modifier.align(Alignment.End)) {
                        Text("Simpan Perubahan")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Change Password Section
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ubah Password", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.currentPassword,
                        onValueChange = viewModel::onCurrentPasswordChange,
                        label = { Text("Password Saat Ini") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.newPassword,
                        onValueChange = viewModel::onNewPasswordChange,
                        label = { Text("Password Baru") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.confirmNewPassword,
                        onValueChange = viewModel::onConfirmNewPasswordChange,
                        label = { Text("Konfirmasi Password Baru") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = uiState.newPassword != uiState.confirmNewPassword
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.changePassword() }, modifier = Modifier.align(Alignment.End)) {
                        Text("Ubah Password")
                    }
                }
            }
        }
    }
}
