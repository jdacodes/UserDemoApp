package com.jdacodes.userdemo.di

import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.core.utils.Constants
import com.jdacodes.userdemo.profile.data.remote.ProfileApiService
import com.jdacodes.userdemo.profile.data.repository.ProfileRepositoryImpl
import com.jdacodes.userdemo.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideNetworkApi(): ProfileApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideProfileRepository(
        authPreferences: AuthPreferences,
        profileApiService: ProfileApiService
    ): ProfileRepository {
        return ProfileRepositoryImpl(authPreferences, profileApiService)
    }
}


