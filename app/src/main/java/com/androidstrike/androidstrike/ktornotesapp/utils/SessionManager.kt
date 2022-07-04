package com.androidstrike.androidstrike.ktornotesapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.JWT_TOKEN_KEY
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.USER_EMAIL_KEY
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.USER_NAME_KEY
import kotlinx.coroutines.flow.first

/**
 * Created by Richard Uzor  on 02/07/2022
 */
/**
 * In this class, we will use this class to manage sessions
 * By creating a function to store and retrieve JWT token using data store
 */
class SessionManager(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session_manager")

    suspend fun updateSession(token: String, name: String, email: String) {
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val userName = stringPreferencesKey(USER_NAME_KEY)
        val userEmail = stringPreferencesKey(USER_EMAIL_KEY)
        context.dataStore.edit { preferences ->
            preferences[jwtTokenKey] = token
            preferences[userName] = name
            preferences[userEmail] = email
        }
    }

    suspend fun getJwtToken(): String? {
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[jwtTokenKey]
    }

    suspend fun getCurrentUserName(): String? {
        val userNameKey = stringPreferencesKey(USER_NAME_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[userNameKey]
    }

    suspend fun getCurrentUserEmail(): String? {
        val userEmailKey = stringPreferencesKey(USER_EMAIL_KEY)
        val preferences = context.dataStore.data.first()

        return preferences[userEmailKey]
    }

    suspend fun logout() {
        context.dataStore.edit {
            it.clear()
        }
    }
}