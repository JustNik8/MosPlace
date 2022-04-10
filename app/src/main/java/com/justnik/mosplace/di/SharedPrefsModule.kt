package com.justnik.mosplace.di

import android.content.Context
import android.content.SharedPreferences
import com.justnik.mosplace.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
annotation class TypePreferences

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefsModule {
    @Provides
    @TypePreferences
    fun provideTypePreference(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(
            context.getString(R.string.preference_key_types), Context.MODE_PRIVATE
        )
    }
}