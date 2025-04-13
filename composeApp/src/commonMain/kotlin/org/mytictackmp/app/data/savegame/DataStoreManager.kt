package org.mytictackmp.app.data.savegame

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first

interface DataStoreManager {
    enum class PreferenceKey {
        SAVED_GAME
    }

    suspend fun store(preferenceKey: PreferenceKey, value: String): Result<Unit>

    suspend fun get(preferenceKey: PreferenceKey): Result<String?>
}


class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : DataStoreManager {

    override suspend fun store(
        preferenceKey: DataStoreManager.PreferenceKey,
        value: String
    ): Result<Unit> = coroutineScope.async {
        return@async try {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(preferenceKey.name)] = value
            }
            Result.success(Unit)
        } catch (exception: Throwable) {
            Result.failure(exception)
        }
    }.await()

    override suspend fun get(preferenceKey: DataStoreManager.PreferenceKey): Result<String?> =
        coroutineScope.async {
            try {
                val preferences = dataStore.data.first()
                Result.success(preferences[stringPreferencesKey(preferenceKey.name)])
            } catch (exception: Throwable) {
                Result.failure(exception)
            }
        }.await()
}