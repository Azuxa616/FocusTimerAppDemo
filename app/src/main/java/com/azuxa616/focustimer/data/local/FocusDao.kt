package com.azuxa616.focustimer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azuxa616.focustimer.data.model.FocusRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: FocusRecord)

    @Query("SELECT * FROM focus_records WHERE startTimeMillis BETWEEN :start AND :end ORDER BY startTimeMillis DESC")
    fun recordsBetween(start: Long, end: Long): Flow<List<FocusRecord>>
}

