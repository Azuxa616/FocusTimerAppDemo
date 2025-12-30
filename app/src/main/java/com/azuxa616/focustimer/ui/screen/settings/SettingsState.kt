package com.azuxa616.focustimer.ui.screen.settings

import com.azuxa616.focustimer.data.model.Task

/**
 * 设置页面UI状态
 *
 * @param tasks 事项列表
 * @param enableVibration 是否启用震动
 * @param isSaving 是否正在保存
 * @param editingTask 正在编辑的事项（可为null）
 * @param isExecutingScript 是否正在执行SQL脚本
 * @param scriptExecutionMessage SQL脚本执行结果消息（可为null）
 */
data class SettingsState(
    val tasks: List<Task>,
    val enableVibration: Boolean,
    val isSaving: Boolean,
    val editingTask: Task? = null,
    val isExecutingScript: Boolean = false,
    val scriptExecutionMessage: String? = null
) {
    companion object {
        val Empty = SettingsState(
            tasks = emptyList(),
            enableVibration = true,
            isSaving = false,
            editingTask = null,
            isExecutingScript = false,
            scriptExecutionMessage = null
        )
    }
}

