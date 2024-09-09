package com.jdacodes.userdemo.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.jdacodes.userdemo.auth.util.Constants.AUTH_KEY
import com.jdacodes.userdemo.auth.util.Constants.USER_DATA
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {
    suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = accessToken
        }
    }

    val getAccessToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[AUTH_KEY] ?: ""
    }

    suspend fun clearAccessToken() {
        dataStore.edit { preferences ->
            preferences[AUTH_KEY] = ""
        }
    }

    suspend fun saveUserdata(user: UserDto) {
        dataStore.edit { preferences ->
            preferences[USER_DATA] = gson.toJson(user)
        }
    }

    val getUserData: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_DATA] ?: ""
    }
}