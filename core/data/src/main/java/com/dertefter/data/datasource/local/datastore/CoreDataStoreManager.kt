package com.dertefter.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoreDataStoreManager(private val context: Context) {

    companion object {
        private const val CORE_DATASTORE_NAME = "core_datastore"

        private val APP_CURRENT_LOGIN = stringPreferencesKey("app_current_user")
        private val LOGIN_HISTORY = stringSetPreferencesKey("login_history")
    }

    val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(CORE_DATASTORE_NAME) }
        )
    }

    suspend fun switchToLogin(login: String) {
        dataStore.edit { preferences ->
            if (login.isEmpty()) {
                preferences.remove(APP_CURRENT_LOGIN)
            } else {
                preferences[APP_CURRENT_LOGIN] = login
            }
        }
    }


    suspend fun logout(){
        dataStore.edit { preferences ->
            preferences.remove(APP_CURRENT_LOGIN)

        }
    }


    fun getCurrentLogin(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[APP_CURRENT_LOGIN]
        }
    }


    fun getLoginHistory(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_HISTORY]?.toList() ?: emptyList()
        }
    }

    suspend fun addLoginToHistory(login: String) {
        dataStore.edit { preferences ->
            val current = preferences[LOGIN_HISTORY] ?: emptySet()
            preferences[LOGIN_HISTORY] = current + login
        }
    }

    suspend fun removeLoginFromHistory(login: String) {
        dataStore.edit { preferences ->
            val current = preferences[LOGIN_HISTORY] ?: emptySet()
            preferences[LOGIN_HISTORY] = current - login
        }
    }

    suspend fun clearLoginHistory() {
        dataStore.edit { preferences ->
            preferences.remove(LOGIN_HISTORY)
        }
    }
}