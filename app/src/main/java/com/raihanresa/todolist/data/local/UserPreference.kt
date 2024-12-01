package com.raihanresa.todolist.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreference(context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    private val USER_ID_KEY = intPreferencesKey("user_id")
    private val USERNAME_KEY = stringPreferencesKey("username")

    suspend fun saveData(id: Int, username: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
            preferences[USERNAME_KEY] = username
        }
    }

    val userIdFlow: Flow<Int?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    val usernameFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreference(context.applicationContext)
            }.also { INSTANCE = it }
        }
    }
}
