package com.jdacodes.userdemo.di

import android.app.Application
import androidx.room.Room
import com.jdacodes.userdemo.core.utils.Constants
import com.jdacodes.userdemo.userlist.data.local.UserDatabase
import com.jdacodes.userdemo.userlist.data.remote.UserApiService
import com.jdacodes.userdemo.userlist.data.repository.UserRepositoryImpl
import com.jdacodes.userdemo.userlist.domain.repository.UserRepository
import com.jdacodes.userdemo.userlist.domain.use_case.GetSingleUserCase
import com.jdacodes.userdemo.userlist.domain.use_case.GetUserListCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideGetSingleUserUseCase(repository: UserRepository): GetSingleUserCase {
        return GetSingleUserCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserListUseCase(repository: UserRepository): GetUserListCase {
        return GetUserListCase(repository)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        db: UserDatabase,
        api: UserApiService
    ): UserRepository {
        return UserRepositoryImpl(api, db.userDao())
    }

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            "user_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideNetworkApi(): UserApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

}