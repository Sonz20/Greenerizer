package com.dicoding.greenerizer.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

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