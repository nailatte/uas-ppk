package com.example.sipora.data.remote.interceptor

import com.example.sipora.data.local.datastore.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Add Singleton annotation if it's meant to be a singleton
class HeaderInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { sessionManager.tokenFlow.first() }
        val request = if (!token.isNullOrBlank()) {
            val tokenType = runBlocking { sessionManager.tokenTypeFlow.first() } ?: "Bearer"
            chain.request().newBuilder()
                .header("Authorization", "$tokenType $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
