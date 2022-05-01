package com.justnik.mosplace.di

import com.justnik.mosplace.data.repositories.AuthRepositoryImpl
import com.justnik.mosplace.data.repositories.MosRepositoryImpl
import com.justnik.mosplace.data.repositories.ProfileRepositoryImpl
import com.justnik.mosplace.domain.AuthRepository
import com.justnik.mosplace.domain.MosRepository
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
    fun bindMosRepository(impl: MosRepositoryImpl): MosRepository

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun findProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}