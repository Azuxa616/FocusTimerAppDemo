package com.azuxa616.focustimer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.azuxa616.focustimer.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * 事项数据访问对象
 *
 * 提供事项的数据库操作接口
 */
@Dao
interface TaskDao {
    /**
     * 查询所有事项
     */
    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * 根据ID查询事项
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<Task?>

    /**
     * 插入事项
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    /**
     * 更新事项
     */
    @Update
    suspend fun update(task: Task)

    /**
     * 删除事项
     */
    @Delete
    suspend fun delete(task: Task)
}

