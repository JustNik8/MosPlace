package com.justnik.mosplace.di

import android.app.Application
import androidx.room.Room
import com.justnik.mosplace.data.database.MosPlaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DB_NAME = "mosplace.db"

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MosPlaceDatabase {
        return Room.databaseBuilder(app, MosPlaceDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}