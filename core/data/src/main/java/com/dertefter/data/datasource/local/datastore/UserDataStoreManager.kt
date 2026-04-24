package com.dertefter.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.dertefter.data.common.Constants.GUEST_DATASTORE_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class UserDataStoreManager(private val context: Context) {


    private val stores = ConcurrentHashMap<String, DataStore<Preferences>>()

    private val _currentStore = MutableStateFlow(getStore(GUEST_DATASTORE_NAME))
    val currentStore: StateFlow<DataStore<Preferences>> = _currentStore.asStateFlow()

    fun switchToLogin(userLogin: String?) {
        _currentStore.value = getStore(userLogin ?: GUEST_DATASTORE_NAME)
    }

    private fun getStore(name: String): DataStore<Preferences> =
        stores.computeIfAbsent(name) { key ->
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile(key) }
            )
        }

    private fun Context.preferencesDataStoreFile(name: String): File =
        this.filesDir.resolve("datastore/$name.preferences_pb")
}