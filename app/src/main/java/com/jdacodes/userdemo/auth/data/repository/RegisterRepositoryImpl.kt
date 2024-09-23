package com.jdacodes.userdemo.auth.data.repository

import android.util.Log
import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.auth.data.remote.AuthApiService
import com.jdacodes.userdemo.auth.data.remote.request.RegistrationRequest
import com.jdacodes.userdemo.auth.domain.repository.RegisterRepository
import com.jdacodes.userdemo.auth.util.parseErrorMessage
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.userlist.data.remote.UserApiService
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto
import retrofit2.HttpException
import java.io.IOException

class RegisterRepositoryImpl(
    private val authApiService: AuthApiService,
    private val userApiService: UserApiService,
    private val authPreferences: AuthPreferences
) : RegisterRepository {
    override suspend fun register(registrationRequest: RegistrationRequest): Resource<Unit> {
        return try {
            val response = authApiService.registerUser(registrationRequest)
            if (response.isSuccessful) {
                val userId = response.body()?.id
                if (userId != null && userId > 0) {
                    findUserById(userId).let {
                        if (it != null) {
                            authPreferences.saveUserdata(it)
                        }
                    }
                }
                val token = response.body()?.token
                if (token != null) {
                    authPreferences.saveAccessToken(token)
                }
                Resource.Success(Unit)  // Registration successful
            } else {
                // Handle error response
                val errorResponse = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorResponse)
                Resource.Error(message = errorMessage)
            }

        } catch (e: IOException) {
            Log.d("RegisterRepositoryImpl", "Register error: ${e.message}")
            Resource.Error(message = "Could not reach the server, please check your internet connection!")
        } catch (e: HttpException) {
            Log.d("RegisterRepositoryImpl", "Register error: ${e.message}")
            Resource.Error(message = "An Unknown error occurred, please try again!")
        }
    }

    private suspend fun findUserById(id: Int): UserDto? {
        var currentPage = 1
        var totalPages: Int

        return try {
            do {
                // Fetch users for the current page
                val response = userApiService.getUsers(currentPage)
                totalPages = response.totalPages

                // Try to find the user with the given email
                val user = response.data.find { it.id == id }
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
            Log.e("RegisterRepositoryImpl", "Error fetching user by ID: ${e.message}")
            null
        }
    }
}