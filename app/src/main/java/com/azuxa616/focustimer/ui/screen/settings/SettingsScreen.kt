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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.data.model.Task

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

            // 震动设置
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
                        checked = state.enableVibration,
                        onCheckedChange = { viewModel.onEnableVibrationChanged(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 事项管理标题
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "事项管理",
                    style = MaterialTheme.typography.titleLarge
                )
                Button(
                    onClick = { viewModel.startEditingTask(Task(0, "", 25, 5, 1)) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("添加事项")
                }
            }

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
                
                // 导入测试数据按钮
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    ImportTestDataSection(
                        isExecuting = state.isExecutingScript,
                        executionMessage = state.scriptExecutionMessage,
                        onImportClick = { viewModel.executeTestDataScript() },
                        onDismissMessage = { viewModel.clearScriptExecutionMessage() }
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
        AlertDialog(
            onDismissRequest = { viewModel.clearScriptExecutionMessage() },
            title = { Text("导入测试数据") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearScriptExecutionMessage() }) {
                    Text("确定")
                }
            }
        )
    }
}

/**
 * 导入测试数据区域
 */
@Composable
private fun ImportTestDataSection(
    isExecuting: Boolean,
    executionMessage: String?,
    onImportClick: () -> Unit,
    onDismissMessage: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "测试数据",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "导入测试数据用于开发和测试",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isExecuting) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(16.dp).height(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("正在导入...")
                }
            } else {
                OutlinedButton(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("导入测试数据")
                }
            }
        }
    }
    
    // 确认对话框
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("确认导入") },
            text = {
                Column {
                    Text("此操作将：")
                    Text("1. 清除现有的任务和会话数据")
                    Text("2. 插入5个测试任务和约40条会话记录")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "确定要继续吗？",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onImportClick()
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

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

@Composable
private fun TaskEditDialog(
    task: Task,
    onSave: (String, Int, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(if (task.id == 0L) "" else task.name) }
    var focusMinutes by remember { mutableStateOf(if (task.id == 0L) "25" else task.defaultFocusMinutes.toString()) }
    var breakMinutes by remember { mutableStateOf(if (task.id == 0L) "5" else task.defaultBreakMinutes.toString()) }
    var cycles by remember { mutableStateOf(if (task.id == 0L) "1" else task.defaultCycles.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task.id == 0L) "添加事项" else "编辑事项") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("事项名称") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = focusMinutes,
                    onValueChange = { focusMinutes = it },
                    label = { Text("专注时间（分钟）") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = breakMinutes,
                    onValueChange = { breakMinutes = it },
                    label = { Text("休息时间（分钟）") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cycles,
                    onValueChange = { cycles = it },
                    label = { Text("循环次数") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val focus = focusMinutes.toIntOrNull() ?: 0
                    val breakTime = breakMinutes.toIntOrNull() ?: 0
                    val cycle = cycles.toIntOrNull() ?: 0
                    if (name.isNotBlank() && focus > 0 && breakTime > 0 && cycle > 0) {
                        onSave(name, focus, breakTime, cycle)
                    }
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
