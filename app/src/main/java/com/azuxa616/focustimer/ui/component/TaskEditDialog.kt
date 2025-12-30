package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.data.model.Task

/**
 * 任务编辑对话框
 *
 * 用于添加或编辑事项的对话框组件
 *
 * @param task 要编辑的任务（id为0表示新建）
 * @param onSave 保存回调，参数为(名称, 专注时间, 休息时间, 循环次数)
 * @param onDismiss 取消回调
 */
@Composable
fun TaskEditDialog(
    task: Task,
    onSave: (String, Int, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val isNewTask = task.id == 0L

    var name by remember { mutableStateOf(if (isNewTask) "" else task.name) }
    var focusMinutes by remember { mutableStateOf(if (isNewTask) "25" else task.defaultFocusMinutes.toString()) }
    var breakMinutes by remember { mutableStateOf(if (isNewTask) "5" else task.defaultBreakMinutes.toString()) }
    var cycles by remember { mutableStateOf(if (isNewTask) "1" else task.defaultCycles.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNewTask) "添加事项" else "编辑事项") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("事项名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = focusMinutes,
                    onValueChange = { focusMinutes = it },
                    label = { Text("专注时间（分钟）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = breakMinutes,
                    onValueChange = { breakMinutes = it },
                    label = { Text("休息时间（分钟）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cycles,
                    onValueChange = { cycles = it },
                    label = { Text("循环次数") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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

