package com.example.sipora.data.remote.interceptor

import com.example.sipora.data.local.datastore.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        val path = originalRequest.url.encodedPath

        val isPublicEndpoint = path.contains("/auth/login") || path.contains("/auth/register")

        // If it's a public endpoint, proceed without modification.
        if (isPublicEndpoint) {
            return chain.proceed(requestBuilder.build())
        }

        // For protected endpoints, block to get the token and add the header.
        val token = runBlocking { sessionManager.tokenFlow.first() }
        
        if (!token.isNullOrBlank()) {
            val tokenType = runBlocking { sessionManager.tokenTypeFlow.first() } ?: "Bearer"
            requestBuilder.addHeader("Authorization", "$tokenType $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
