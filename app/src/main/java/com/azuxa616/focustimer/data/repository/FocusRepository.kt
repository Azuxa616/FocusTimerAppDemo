package com.azuxa616.focustimer.data.repository

import com.azuxa616.focustimer.data.local.FocusSessionDao
import com.azuxa616.focustimer.data.model.FocusSession
import kotlinx.coroutines.flow.Flow

/**
 * 专注会话仓库
 *
 * 封装专注会话的数据访问操作
 */
class FocusRepository(
    private val focusSessionDao: FocusSessionDao
) {
    /**
     * 添加专注会话记录
     */
    suspend fun addFocusSession(session: FocusSession): Long {
        return focusSessionDao.insert(session)
    }

    /**
     * 查询时间范围内的会话
     */
    fun sessionsBetween(start: Long, end: Long): Flow<List<FocusSession>> {
        return focusSessionDao.sessionsBetween(start, end)
    }

    /**
     * 根据taskId查询会话
     */
    fun sessionsByTaskId(taskId: Long): Flow<List<FocusSession>> {
        return focusSessionDao.sessionsByTaskId(taskId)
    }

    /**
     * 获取所有会话
     */
    fun getAllSessions(): Flow<List<FocusSession>> {
        return focusSessionDao.getAllSessions()
    }

    /**
     * 删除专注会话记录
     */
    suspend fun deleteFocusSession(session: FocusSession) {
        focusSessionDao.delete(session)
    }
}

