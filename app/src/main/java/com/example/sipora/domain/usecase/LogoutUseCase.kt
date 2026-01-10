package com.example.sipora.domain.usecase

import com.example.sipora.data.local.datastore.SessionManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        sessionManager.clearSession()
    }
}
