package com.azuxa616.focustimer.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "focus_timer_settings")

data class TimerSettings(
    val focusMinutes: Int,
    val breakMinutes: Int,
    val enableVibration: Boolean
)

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    val settingsFlow: Flow<TimerSettings> = dataStore.data.map { preferences ->
        TimerSettings(
            focusMinutes = preferences[KEY_FOCUS_MINUTES] ?: DEFAULT_FOCUS_MINUTES,
            breakMinutes = preferences[KEY_BREAK_MINUTES] ?: DEFAULT_BREAK_MINUTES,
            enableVibration = preferences[KEY_ENABLE_VIBRATION] ?: true
        )
    }

    suspend fun updateFocusMinutes(minutes: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_FOCUS_MINUTES] = minutes
        }
    }

    suspend fun updateBreakMinutes(minutes: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_BREAK_MINUTES] = minutes
        }
    }

    suspend fun updateEnableVibration(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_ENABLE_VIBRATION] = enabled
        }
    }

    companion object {
        private val KEY_FOCUS_MINUTES = intPreferencesKey("focus_minutes")
        private val KEY_BREAK_MINUTES = intPreferencesKey("break_minutes")
        private val KEY_ENABLE_VIBRATION = booleanPreferencesKey("enable_vibration")
        private const val DEFAULT_FOCUS_MINUTES = 25
        private const val DEFAULT_BREAK_MINUTES = 5

        fun fromContext(context: Context): SettingsRepository {
            return SettingsRepository(context.settingsDataStore)
        }
    }
}

