package com.example.sipora.di

import com.example.sipora.core.config.Constants
import com.example.sipora.data.local.datastore.SessionManager
import com.example.sipora.data.remote.api.*
import com.example.sipora.data.remote.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHeaderInterceptor(sessionManager: SessionManager): HeaderInterceptor {
        return HeaderInterceptor(sessionManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ... other provides
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSuperAdminService(retrofit: Retrofit): SuperAdminService {
        return retrofit.create(SuperAdminService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    @Singleton
    fun providePeriodePendaftaranService(retrofit: Retrofit): PeriodePendaftaranService {
        return retrofit.create(PeriodePendaftaranService::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminService(retrofit: Retrofit): AdminService {
        return retrofit.create(AdminService::class.java)
    }

    @Provides
    @Singleton
    fun providePeriodeWawancaraService(retrofit: Retrofit): PeriodeWawancaraService {
        return retrofit.create(PeriodeWawancaraService::class.java)
    }
}
