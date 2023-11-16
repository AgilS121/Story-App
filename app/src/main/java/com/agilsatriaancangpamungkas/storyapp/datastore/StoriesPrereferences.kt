package com.agilsatriaancangpamungkas.storyapp.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.agilsatriaancangpamungkas.storyapp.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class StoriesPrereferences private constructor(private val dataStore: DataStore<Preferences>){

    private val authToken = stringPreferencesKey("auth_token")
    private val nameUser = stringPreferencesKey("name_user")
    private val userID = stringPreferencesKey("user_id")

    fun getToken() : Flow<String> {
        return dataStore.data.map { prefrences ->
            prefrences[authToken] ?: ""
        }
    }

    fun getSession(): Flow<LoginResult> {
        return dataStore.data.map { preferences ->
            val nameUser = preferences[nameUser] ?: ""
            val userID = preferences[userID] ?: ""
            val authToken = preferences[authToken] ?: ""
            val loginResult = LoginResult(nameUser, userID, authToken)
            Log.d("initokenprefrences", authToken)
            loginResult
        }
    }


    suspend fun saveSession(token: LoginResult) {
        dataStore.edit { preferences ->
            preferences[nameUser] = token.name
            preferences[userID] = token.userId
            preferences[authToken] = token.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[nameUser] = ""
            preferences[userID] = ""
            preferences[authToken] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoriesPrereferences? = null
        fun getInstance(dataStore: DataStore<Preferences>) : StoriesPrereferences {
            return INSTANCE ?: synchronized(this) {
                val instance = StoriesPrereferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}