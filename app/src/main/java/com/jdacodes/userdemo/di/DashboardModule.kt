package com.jdacodes.userdemo.di

import android.app.Application
import androidx.room.Room
import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.core.utils.Constants
import com.jdacodes.userdemo.dashboard.data.local.ColorDatabase
import com.jdacodes.userdemo.dashboard.data.remote.ColorApiService
import com.jdacodes.userdemo.dashboard.data.repository.ColorRepositoryImpl
import com.jdacodes.userdemo.dashboard.domain.repository.ColorRepository
import com.jdacodes.userdemo.dashboard.domain.use_case.GetColorListCase
import com.jdacodes.userdemo.dashboard.domain.use_case.GetSingleColorCase
import com.jdacodes.userdemo.dashboard.domain.use_case.GetUserProfileCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    @Provides
    @Singleton
    fun provideUserProfileUseCase(repository: ColorRepository): GetUserProfileCase {
        return GetUserProfileCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSingleColorUseCase(repository: ColorRepository): GetSingleColorCase {
        return GetSingleColorCase(repository)
    }

    @Provides
    @Singleton
    fun provideColorListCase(repository: ColorRepository): GetColorListCase {
        return GetColorListCase(repository)
    }

    @Provides
    @Singleton
    fun provideColorRepository(
        db: ColorDatabase,
        api: ColorApiService,
        authPreferences: AuthPreferences
    ): ColorRepository {
        return ColorRepositoryImpl(api, db.colorDao(),authPreferences)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ): ColorDatabase {
        return Room.databaseBuilder(
            app,
            ColorDatabase::class.java,
            "color_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkApi(): ColorApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ColorApiService::class.java)
    }
}