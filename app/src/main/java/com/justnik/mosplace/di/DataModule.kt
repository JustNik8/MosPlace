package com.justnik.mosplace.di

import com.justnik.mosplace.data.repository.AuthRepositoryImpl
import com.justnik.mosplace.data.repository.MosRepositoryImpl
import com.justnik.mosplace.domain.AuthRepository
import com.justnik.mosplace.domain.MosRepository
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
}