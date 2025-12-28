package com.azuxa616.focustimer.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.data.repository.SettingsRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 设置页面ViewModel
 *
 * 管理事项列表和设置
 */
class SettingsViewModel(
    private val taskRepository: TaskRepository,
    private val settingsRepository: SettingsRepository
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

    companion object {
        class Factory(
            private val taskRepository: TaskRepository,
            private val settingsRepository: SettingsRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(taskRepository, settingsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
