package com.example.sipora.di

import com.example.sipora.data.repository.*
import com.example.sipora.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSuperAdminRepository(superAdminRepositoryImpl: SuperAdminRepositoryImpl): SuperAdminRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindPeriodePendaftaranRepository(periodePendaftaranRepositoryImpl: PeriodePendaftaranRepositoryImpl): PeriodePendaftaranRepository

    @Binds
    @Singleton
    abstract fun bindAdminRepository(adminRepositoryImpl: AdminRepositoryImpl): AdminRepository
}
