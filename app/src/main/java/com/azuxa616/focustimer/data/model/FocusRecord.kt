package com.azuxa616.focustimer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_records")
data class FocusRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTimeMillis: Long,
    val durationMinutes: Int,
    val completed: Boolean
)

