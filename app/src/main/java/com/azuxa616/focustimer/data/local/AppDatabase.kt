package com.azuxa616.focustimer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.data.model.Task

@Database(
    entities = [Task::class, FocusSession::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val value = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focus_timer.db"
                )
                    .fallbackToDestructiveMigration() // 由于结构变化较大，使用破坏性迁移
                    .build()
                instance = value
                value
            }
        }
    }
}

