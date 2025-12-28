package com.azuxa616.focustimer.data.repository

import com.azuxa616.focustimer.data.local.TaskDao
import com.azuxa616.focustimer.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * 事项仓库
 *
 * 封装事项的数据访问操作
 */
class TaskRepository(
    private val taskDao: TaskDao
) {
    /**
     * 获取所有事项的Flow
     */
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    /**
     * 根据ID获取事项
     */
    fun getTaskById(id: Long): Flow<Task?> = taskDao.getTaskById(id)

    /**
     * 插入事项
     */
    suspend fun insertTask(task: Task): Long = taskDao.insert(task)

    /**
     * 更新事项
     */
    suspend fun updateTask(task: Task) = taskDao.update(task)

    /**
     * 删除事项
     */
    suspend fun deleteTask(task: Task) = taskDao.delete(task)
}

