package com.justnik.mosplace.di

import com.justnik.mosplace.data.repositories.*
import com.justnik.mosplace.domain.repositories.AuthRepository
import com.justnik.mosplace.domain.repositories.DataRepository
import com.justnik.mosplace.domain.repositories.ProfileRepository
import com.justnik.mosplace.domain.repositories.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
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
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun bindReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository


}