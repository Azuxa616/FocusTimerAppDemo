package com.azuxa616.focustimer.ui.screen.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azuxa616.focustimer.data.SqlScriptExecutor
import com.azuxa616.focustimer.data.local.AppDatabase
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.data.repository.SettingsRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 设置页面ViewModel
 *
 * 管理事项列表和设置
 */
class SettingsViewModel(
    private val taskRepository: TaskRepository,
    private val settingsRepository: SettingsRepository,
    private val context: Context,
    private val database: AppDatabase
) : ViewModel() {
    private val mutableState = MutableStateFlow(SettingsState.Empty)
    val state: StateFlow<SettingsState> = mutableState

    init {
        // 监听事项列表变化
        viewModelScope.launch {
            taskRepository.allTasks.collect { tasks ->
                mutableState.value = mutableState.value.copy(tasks = tasks)
            }
        }

        // 监听震动设置变化
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                mutableState.value = mutableState.value.copy(
                    enableVibration = settings.enableVibration
                )
            }
        }
    }

    /**
     * 更新震动设置
     */
    fun onEnableVibrationChanged(enabled: Boolean) {
        mutableState.value = mutableState.value.copy(enableVibration = enabled)
        viewModelScope.launch {
            settingsRepository.updateEnableVibration(enabled)
        }
    }

    /**
     * 开始编辑事项
     */
    fun startEditingTask(task: Task) {
        mutableState.value = mutableState.value.copy(editingTask = task)
    }

    /**
     * 取消编辑事项
     */
    fun cancelEditingTask() {
        mutableState.value = mutableState.value.copy(editingTask = null)
    }

    /**
     * 添加新事项
     */
    fun addTask(name: String, focusMinutes: Int, breakMinutes: Int, cycles: Int) {
        if (name.isBlank() || focusMinutes <= 0 || breakMinutes <= 0 || cycles <= 0) {
            return
        }
        viewModelScope.launch {
            val task = Task(
                name = name.trim(),
                defaultFocusMinutes = focusMinutes,
                defaultBreakMinutes = breakMinutes,
                defaultCycles = cycles
            )
            taskRepository.insertTask(task)
        }
    }

    /**
     * 更新事项
     */
    fun updateTask(task: Task, name: String, focusMinutes: Int, breakMinutes: Int, cycles: Int) {
        if (name.isBlank() || focusMinutes <= 0 || breakMinutes <= 0 || cycles <= 0) {
            return
        }
        viewModelScope.launch {
            val updatedTask = task.copy(
                name = name.trim(),
                defaultFocusMinutes = focusMinutes,
                defaultBreakMinutes = breakMinutes,
                defaultCycles = cycles
            )
            taskRepository.updateTask(updatedTask)
            mutableState.value = mutableState.value.copy(editingTask = null)
        }
    }

    /**
     * 删除事项
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    /**
     * 执行测试数据SQL脚本
     */
    fun executeTestDataScript() {
        viewModelScope.launch {
            mutableState.value = mutableState.value.copy(
                isExecutingScript = true,
                scriptExecutionMessage = null
            )
            
            val result = withContext(Dispatchers.IO) {
                val sqliteDatabase = database.getWritableDatabase()
                SqlScriptExecutor.executeScript(
                    context = context,
                    database = sqliteDatabase,
                    scriptFileName = "test_data.sql"
                )
            }
            
            mutableState.value = mutableState.value.copy(
                isExecutingScript = false,
                scriptExecutionMessage = result.fold(
                    onSuccess = { it },
                    onFailure = { "执行失败: ${it.message}" }
                )
            )
        }
    }

    /**
     * 清除脚本执行消息
     */
    fun clearScriptExecutionMessage() {
        mutableState.value = mutableState.value.copy(scriptExecutionMessage = null)
    }

    companion object {
        class Factory(
            private val taskRepository: TaskRepository,
            private val settingsRepository: SettingsRepository,
            private val context: Context,
            private val database: AppDatabase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(taskRepository, settingsRepository, context, database) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
