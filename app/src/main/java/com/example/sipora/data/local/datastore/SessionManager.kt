package com.example.sipora.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager @Inject constructor(context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveAuth(token: String, type: String, role: String, divisiId: Long?, ormawaId: Long?) {
        dataStore.edit {
            it[TOKEN_KEY] = token
            it[TYPE_KEY] = type
            it[ROLE_KEY] = role
            if (divisiId != null) it[DIVISI_ID_KEY] = divisiId
            if (ormawaId != null) it[ORMAWA_ID_KEY] = ormawaId
        }
    }

    val tokenFlow: Flow<String?> get() = dataStore.data.map { it[TOKEN_KEY] }
    val tokenTypeFlow: Flow<String?> get() = dataStore.data.map { it[TYPE_KEY] }
    val roleFlow: Flow<String?> get() = dataStore.data.map { it[ROLE_KEY] }
    val divisiIdFlow: Flow<Long?> get() = dataStore.data.map { it[DIVISI_ID_KEY] }
    val ormawaIdFlow: Flow<Long?> get() = dataStore.data.map { it[ORMAWA_ID_KEY] }

    suspend fun clearSession() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val TYPE_KEY = stringPreferencesKey("auth_type")
        private val ROLE_KEY = stringPreferencesKey("auth_role")
        private val DIVISI_ID_KEY = longPreferencesKey("divisi_id")
        private val ORMAWA_ID_KEY = longPreferencesKey("ormawa_id")
    }
}
