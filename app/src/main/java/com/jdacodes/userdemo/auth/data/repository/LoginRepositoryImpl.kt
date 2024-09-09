package com.jdacodes.userdemo.auth.data.repository

import android.util.Log
import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.auth.data.remote.AuthApiService
import com.jdacodes.userdemo.auth.data.remote.request.LoginRequest
import com.jdacodes.userdemo.auth.domain.model.LoginResult
import com.jdacodes.userdemo.auth.domain.repository.LoginRepository
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.userlist.data.remote.UserApiService
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class LoginRepositoryImpl(
    private val authApiService: AuthApiService,
    private val userApiService: UserApiService,
    private val authPreferences: AuthPreferences
) : LoginRepository {
    override suspend fun login(
        loginRequest: LoginRequest,
        rememberMe: Boolean
    ): Resource<Unit> {
        return try {
            authApiService.loginUser(loginRequest)
            val response = authApiService.loginUser(loginRequest)
            findUserByEmail(loginRequest.username)?.let { authPreferences.saveUserdata(it) }
            if (rememberMe) {
                authPreferences.saveAccessToken(response.token)
            }
            Resource.Success(Unit)
        } catch (e: IOException) {
            Log.d("LoginRepositoryImpl", "Login error: ${e.message}")
            Resource.Error(message = "Could not reach the server, please check your internet connection!")
        } catch (e: HttpException) {
            Log.d("LoginRepositoryImpl", "Login error: ${e.message}")
            Resource.Error(message = "An Unknown error occurred, please try again!")
        }

    }

    override suspend fun autoLogin(): Resource<Unit> {
        val accessToken = authPreferences.getAccessToken.first()
        return if (accessToken != "") {
            Resource.Success(Unit)
        } else {
            Resource.Error("")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            authPreferences.clearAccessToken()
            val fetchedToken = authPreferences.getAccessToken.first()

            if (fetchedToken.isEmpty()) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Unknown Error")
            }
        } catch (e: Exception) {
            return Resource.Error("Unknown error occurred")
        }
    }

    private suspend fun findUserByEmail(email: String): UserDto? {
        var currentPage = 1
        var totalPages: Int

        return try {
            do {
                // Fetch users for the current page
                val response = userApiService.getUsers(currentPage)
                totalPages = response.totalPages

                // Try to find the user with the given email
                val user = response.data.find { it.email == email }
                if (user != null) {
                    return user
                }

                // Move to the next page
                currentPage++
            } while (currentPage <= totalPages)

            // Return null if the user is not found after iterating through all pages
            null
        } catch (e: Exception) {
            // Handle exceptions like network failure, etc.
            null
        }
    }

}