package com.dertefter.data.datasource.local

import android.content.Context
import androidx.core.content.edit
import com.dertefter.data.dto.auth.AuthCreditions
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.spght.encryptedprefs.EncryptedSharedPreferences
import dev.spght.encryptedprefs.MasterKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedAuthStorage @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey(context = context)

    private val sharedPreferences = EncryptedSharedPreferences(
        context = context,
        fileName = "auth_prefs",
        masterKey = masterKey
    )

    fun saveAuthCreds(login: String, creds: AuthCreditions) {
        sharedPreferences.edit {
            putString("${login}_password", creds.xPassword)
        }
    }

    fun getAuthCreds(login: String): AuthCreditions? {
        val password = sharedPreferences.getString("${login}_password", null) ?: return null
        return AuthCreditions(login, password)
    }

    fun deleteAuthCreds(login: String) {
        sharedPreferences.edit {
            remove("${login}_password")
        }
    }
}
