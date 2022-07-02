package com.androidstrike.androidstrike.ktornotesapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.androidstrike.androidstrike.ktornotesapp.utils.Constants.JWT_TOKEN_KEY
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

    suspend fun saveJwtToken(token:String){
        val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
        context.dataStore.edit { preferences ->
            preferences[jwtTokenKey] = token
        }

        suspend fun getJwtToken(): String? {
            val jwtTokenKey = stringPreferencesKey(JWT_TOKEN_KEY)
            val preferences = context.dataStore.data.first()

            return preferences[jwtTokenKey]
        }
    }
}