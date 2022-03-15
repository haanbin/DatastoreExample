package com.example.datastoreexample

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.datastoreexample.DatastoreRepository.PreferencesKeys.CLICK_COUNT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DatastoreRepository(
    private val preferencesDataStore: DataStore<Preferences>,
    private val protoDatastore: DataStore<UserProto>
) {

    private object PreferencesKeys {
        val CLICK_COUNT = intPreferencesKey("click_count")
    }

    suspend fun getUserPreferences(): Flow<UserPreferences> = preferencesDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("Error reading preferences.", exception.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updatePreferencesClickCount(clickCount: Int) {
        preferencesDataStore.edit { preferences ->
            preferences[CLICK_COUNT] = clickCount
        }
    }

    suspend fun getUserProto(): Flow<UserProto> = protoDatastore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("Error reading sort order preferences", exception.toString())
                emit(UserProto.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateUserProto(clickCount: Int) {
        protoDatastore.updateData {
            it.toBuilder().setClickCount(clickCount).build()
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get the sort order from preferences and convert it to a [SortOrder] object
        val clickCount = preferences[CLICK_COUNT] ?: DEFAULT_CLICK_COUNT
        return UserPreferences(clickCount)
    }

    companion object {
        const val DEFAULT_CLICK_COUNT = 0
    }
}