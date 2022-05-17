package com.justnik.mosplace.di

import android.app.Application
import androidx.room.Room
import com.justnik.mosplace.data.database.MosPlaceDatabase
import dagger.Provides
import javax.inject.Singleton

object DatabaseModule {
    private const val DB_NAME = "mosplace.db"

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MosPlaceDatabase {
        return Room.databaseBuilder(app, MosPlaceDatabase::class.java, DB_NAME).build()
    }
}