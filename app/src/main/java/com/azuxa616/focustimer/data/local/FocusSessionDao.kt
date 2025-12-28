package com.azuxa616.focustimer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azuxa616.focustimer.data.model.FocusSession
import kotlinx.coroutines.flow.Flow

/**
 * 专注会话数据访问对象
 *
 * 提供专注会话的数据库操作接口
 */
@Dao
interface FocusSessionDao {
    /**
     * 插入会话
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FocusSession): Long

    /**
     * 查询时间范围内的会话
     */
    @Query("SELECT * FROM focus_sessions WHERE startTimeMillis BETWEEN :start AND :end ORDER BY startTimeMillis DESC")
    fun sessionsBetween(start: Long, end: Long): Flow<List<FocusSession>>

    /**
     * 根据taskId查询会话
     */
    @Query("SELECT * FROM focus_sessions WHERE taskId = :taskId ORDER BY startTimeMillis DESC")
    fun sessionsByTaskId(taskId: Long): Flow<List<FocusSession>>

    /**
     * 查询所有会话
     */
    @Query("SELECT * FROM focus_sessions ORDER BY startTimeMillis DESC")
    fun getAllSessions(): Flow<List<FocusSession>>
}

