package com.justnik.mosplace.di

import com.justnik.mosplace.data.repositories.AuthRepositoryImpl
import com.justnik.mosplace.data.repositories.DataRepositoryImpl
import com.justnik.mosplace.data.repositories.ProfileRepositoryImpl
import com.justnik.mosplace.domain.AuthRepository
import com.justnik.mosplace.domain.DataRepository
import com.justnik.mosplace.domain.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindDataRepository(impl: DataRepositoryImpl): DataRepository

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun findProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}