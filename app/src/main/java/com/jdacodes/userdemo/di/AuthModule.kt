package com.jdacodes.userdemo.di

import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.auth.data.remote.AuthApiService
import com.jdacodes.userdemo.auth.data.repository.LoginRepositoryImpl
import com.jdacodes.userdemo.auth.data.repository.RegisterRepositoryImpl
import com.jdacodes.userdemo.auth.data.util.EmailValidatorImpl
import com.jdacodes.userdemo.auth.domain.repository.LoginRepository
import com.jdacodes.userdemo.auth.domain.repository.RegisterRepository
import com.jdacodes.userdemo.auth.domain.use_case.AutoLoginCase
import com.jdacodes.userdemo.auth.domain.use_case.LoginCase
import com.jdacodes.userdemo.auth.domain.use_case.LogoutCase
import com.jdacodes.userdemo.auth.domain.use_case.RegisterCase
import com.jdacodes.userdemo.auth.domain.use_case.ValidateConfirmCase
import com.jdacodes.userdemo.auth.domain.use_case.ValidateEmailCase
import com.jdacodes.userdemo.auth.domain.use_case.ValidatePasswordCase
import com.jdacodes.userdemo.auth.util.EmailValidator
import com.jdacodes.userdemo.core.utils.Constants.BASE_URL
import com.jdacodes.userdemo.userlist.data.remote.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideEmailValidator(): EmailValidator = EmailValidatorImpl()

    @Provides
    @Singleton
    fun provideValidateEmail(emailValidator: EmailValidator): ValidateEmailCase {
        return ValidateEmailCase(emailValidator)
    }

    @Provides
    @Singleton
    fun provideValidatePassword(): ValidatePasswordCase {
        return ValidatePasswordCase()
    }

    @Provides
    @Singleton
    fun provideValidateConfirmPassword(): ValidateConfirmCase {
        return ValidateConfirmCase()
    }

    @Provides
    @Singleton
    fun provideLogin(
        loginRepository: LoginRepository,
        validateEmailCase: ValidateEmailCase,
        validatePasswordCase: ValidatePasswordCase
    ): LoginCase {
        return LoginCase(
            loginRepository = loginRepository,
            validateEmailUseCase = validateEmailCase,
            validatePasswordUseCase = validatePasswordCase
        )
    }

    @Provides
    @Singleton
    fun provideRegister(
        registerRepository: RegisterRepository,
        validateEmailCase: ValidateEmailCase,
        validatePasswordCase: ValidatePasswordCase,
        validateConfirmCase: ValidateConfirmCase
    ): RegisterCase {
        return RegisterCase(
            registerRepository = registerRepository,
            validateEmailUseCase = validateEmailCase,
            validatePasswordUseCase = validatePasswordCase,
            validateConfirmCase = validateConfirmCase
        )
    }

    @Provides
    @Singleton
    fun provideAutoLoginUseCase(loginRepository: LoginRepository): AutoLoginCase {
        return AutoLoginCase(loginRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(loginRepository: LoginRepository): LogoutCase {
        return LogoutCase(loginRepository)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        authApiService: AuthApiService,
        authPreferences: AuthPreferences,
        userApiService: UserApiService
    ): LoginRepository {
        return LoginRepositoryImpl(
            authApiService = authApiService,
            authPreferences = authPreferences,
            userApiService = userApiService
        )
    }

    @Provides
    @Singleton
    fun provideRegisterRepository(
        authApiService: AuthApiService,
        authPreferences: AuthPreferences,
        userApiService: UserApiService
    ): RegisterRepository {
        return RegisterRepositoryImpl(
            authApiService = authApiService,
            authPreferences = authPreferences,
            userApiService = userApiService
        )
    }

    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}