package com.azuxa616.focustimer.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.ui.component.ImportTestDataSection
import com.azuxa616.focustimer.ui.component.TaskEditDialog

/**
 * 设置页面
 *
 * 显示应用设置和事项管理
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "设置",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 震动设置卡片
            VibrationSettingCard(
                enableVibration = state.enableVibration,
                onVibrationChanged = { viewModel.onEnableVibrationChanged(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 事项管理标题
            TaskManagementHeader(
                onAddClick = { viewModel.startEditingTask(Task(0, "", 25, 5, 1)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 事项列表
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.tasks) { task ->
                    TaskItem(
                        task = task,
                        onEditClick = { viewModel.startEditingTask(task) },
                        onDeleteClick = { viewModel.deleteTask(task) }
                    )
                }

                // 导入测试数据区域
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    ImportTestDataSection(
                        isExecuting = state.isExecutingScript,
                        onImportClick = { viewModel.executeTestDataScript() }
                    )
                }
            }
        }
    }

    // 编辑事项对话框
    state.editingTask?.let { task ->
        TaskEditDialog(
            task = task,
            onSave = { name, focusMinutes, breakMinutes, cycles ->
                if (task.id == 0L) {
                    viewModel.addTask(name, focusMinutes, breakMinutes, cycles)
                } else {
                    viewModel.updateTask(task, name, focusMinutes, breakMinutes, cycles)
                }
            },
            onDismiss = { viewModel.cancelEditingTask() }
        )
    }

    // 执行结果对话框
    state.scriptExecutionMessage?.let { message ->
        ScriptExecutionResultDialog(
            message = message,
            onDismiss = { viewModel.clearScriptExecutionMessage() }
        )
    }
}

/**
 * 震动设置卡片
 */
@Composable
private fun VibrationSettingCard(
    enableVibration: Boolean,
    onVibrationChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "结束时震动提示",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = enableVibration,
                onCheckedChange = onVibrationChanged
            )
        }
    }
}

/**
 * 事项管理标题栏
 */
@Composable
private fun TaskManagementHeader(
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "事项管理",
            style = MaterialTheme.typography.titleLarge
        )
        Button(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = "添加")
            Spacer(modifier = Modifier.width(8.dp))
            Text("添加事项")
        }
    }
}

/**
 * 事项列表项
 */
@Composable
private fun TaskItem(
    task: Task,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "专注: ${task.defaultFocusMinutes}分钟 | 休息: ${task.defaultBreakMinutes}分钟 | 循环: ${task.defaultCycles}次",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "编辑")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "删除")
            }
        }
    }
}

/**
 * 脚本执行结果对话框
 */
@Composable
private fun ScriptExecutionResultDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("导入测试数据") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        }
    )
}
