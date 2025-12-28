package com.azuxa616.focustimer.ui.screen.timer

import com.azuxa616.focustimer.data.model.FocusRecord
import com.azuxa616.focustimer.data.repository.FocusRepository
import com.azuxa616.focustimer.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private val insertedRecords = mutableListOf<FocusRecord>()

private fun createFocusRepository(): FocusRepository {
    val dao = object : com.azuxa616.focustimer.data.local.FocusDao {
        override suspend fun insert(record: FocusRecord) {
            insertedRecords.add(record)
        }

        override fun recordsBetween(start: Long, end: Long): Flow<List<FocusRecord>> {
            return MutableSharedFlow()
        }
    }
    return FocusRepository(dao)
}

private fun createSettingsRepository(): SettingsRepository {
    val dataStore = object : androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> {
        override val data: Flow<androidx.datastore.preferences.core.Preferences>
            get() = MutableSharedFlow()

        override suspend fun updateData(transform: suspend (androidx.datastore.preferences.core.Preferences) -> androidx.datastore.preferences.core.Preferences): androidx.datastore.preferences.core.Preferences {
            throw UnsupportedOperationException()
        }
    }
    return SettingsRepository(dataStore)
}

class TimerViewModelTest {
    @Test
    fun initialState_isFocusAndNotRunning() = runBlocking {
        val viewModel = TimerViewModel(
            repository = createFocusRepository(),
            settingsRepository = createSettingsRepository()
        )
        val state = viewModel.state.first()
        assertTrue(state.isFocus)
        assertFalse(state.isRunning)
    }
}
