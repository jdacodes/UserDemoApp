package com.jdacodes.userdemo.di

import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.profile.data.repository.ProfileRepositoryImpl
import com.jdacodes.userdemo.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideProfileRepository(authPreferences: AuthPreferences): ProfileRepository {
        return ProfileRepositoryImpl(authPreferences)
    }
}