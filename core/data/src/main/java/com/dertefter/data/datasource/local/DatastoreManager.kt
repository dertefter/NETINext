package com.dertefter.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class DatastoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val CURRENT_LOGIN = stringPreferencesKey("current_login")

    fun getCurrentLogin(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[CURRENT_LOGIN]
        }
    }

    suspend fun setCurrentLogin(login: String?) {
        context.dataStore.edit { preferences ->
            if (login == null) {
                preferences.remove(CURRENT_LOGIN)
            } else {
                preferences[CURRENT_LOGIN] = login
            }
        }
    }
}
