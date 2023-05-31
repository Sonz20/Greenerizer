package com.dicoding.greenerizer.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: ""
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY] ?: ""
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USERID_KEY] ?: ""
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = userId
        }
    }

    suspend fun deleteUserInfo() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USERID_KEY = stringPreferencesKey("userid")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}