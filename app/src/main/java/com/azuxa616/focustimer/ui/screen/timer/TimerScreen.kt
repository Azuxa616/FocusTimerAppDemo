/**
 * 番茄钟页面模块
 *
 * 显示番茄钟计时器界面，包括：
 * - 事项选择下拉菜单
 * - 圆形倒计时显示
 * - 当前状态文本
 * - 设置区域（专注时间、休息时间、循环次数）
 * - 控制按钮（暂停/继续、停止、跳至下一阶段）
 */
package com.azuxa616.focustimer.ui.screen.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.ui.component.TimerCircle
import com.azuxa616.focustimer.ui.component.TimerControlButtons
import com.azuxa616.focustimer.util.TimeFormatter

// ==================== 番茄钟页面组件 ====================

/**
 * 番茄钟主页面
 *
 * 职责：
 * 1. 显示事项选择下拉菜单
 * 2. 显示倒计时圆环和时间
 * 3. 显示当前状态（专注中/休息中）和循环信息
 * 4. 提供设置输入（专注时间、休息时间、循环次数）
 * 5. 提供重置为默认设置按钮
 * 6. 提供控制按钮（暂停/继续、停止、跳至下一阶段）
 *
 * @param viewModel 番茄钟ViewModel，管理计时逻辑
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel
) {
    // 收集ViewModel的状态
    val state by viewModel.state.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState(initial = emptyList())

    // 事项下拉菜单展开状态
    var expanded by remember { mutableStateOf(false) }
    
    // 设置区域折叠状态
    var settingsExpanded by remember { mutableStateOf(true) }
    
    // 自动折叠/展开逻辑：专注开始时自动折叠，结束时自动展开
    LaunchedEffect(state.isRunning) {
        if (state.isRunning) {
            // 专注开始时自动折叠
            settingsExpanded = false
        } else {
            // 专注结束时自动展开
            settingsExpanded = true
        }
    }

    // 计算进度百分比（0.0 ~ 1.0）
    val progress = if (state.totalSeconds == 0L) 0f
    else state.remainingSeconds.toFloat() / state.totalSeconds.toFloat()

    // 格式化剩余时间为 MM:SS
    val timeText = TimeFormatter.formatMinutesSeconds(state.remainingSeconds)

    // 根据模式显示状态文本
    val statusText = if (state.isInFocusPhase) "专注中" else "休息中"
    val cycleText = "第 ${state.currentCycleIndex}/${state.currentCycles} 循环"

    // 获取当前选中的事项名称
    val selectedTaskName = allTasks.find { it.id == state.selectedTaskId }?.name ?: "未选择事项"

    // 页面布局
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 事项选择下拉菜单
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedTaskName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("选择事项") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    allTasks.forEach { task ->
                        DropdownMenuItem(
                            text = { Text(task.name) },
                            onClick = {
                                viewModel.selectTask(task.id)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 页面标题
            Text(
                text = "番茄钟",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 圆形倒计时组件
            TimerCircle(
                progress = progress,
                timeText = timeText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 状态文本
            Text(
                text = statusText,
                style = MaterialTheme.typography.titleMedium
            )

            // 循环信息
            Text(
                text = cycleText,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 设置区域（可折叠）
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 设置标题栏（可点击）
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "设置",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(
                        onClick = { settingsExpanded = !settingsExpanded }
                    ) {
                        Icon(
                            imageVector = if (settingsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (settingsExpanded) "折叠" else "展开"
                        )
                    }
                }
                
                // 设置内容（可折叠）
                AnimatedVisibility(
                    visible = settingsExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 专注时间设置
                        SettingRow(
                            label = "专注时间（分钟）",
                            value = state.currentFocusMinutes,
                            onValueChange = { viewModel.updateFocusMinutes(it) },
                            enabled = !state.isRunning,
                            valueRange = 5..60
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 休息时间设置
                        SettingRow(
                            label = "休息时间（分钟）",
                            value = state.currentBreakMinutes,
                            onValueChange = { viewModel.updateBreakMinutes(it) },
                            enabled = !state.isRunning,
                            valueRange = 1..30
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 循环次数设置
                        SettingRow(
                            label = "循环次数",
                            value = state.currentCycles,
                            onValueChange = { viewModel.updateCycles(it) },
                            enabled = !state.isRunning,
                            valueRange = 1..10
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 重置为默认设置按钮
                        Button(
                            onClick = { viewModel.resetToDefaultSettings() },
                            enabled = !state.isRunning && state.selectedTaskId != null
                        ) {
                            Text(text = "重置为默认设置")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 控制按钮组
            TimerControlButtons(
                isRunning = state.isRunning,
                onStartOrPauseClick = { viewModel.startOrResume() },
                onStopClick = { viewModel.stop() },
                onSkipClick = { viewModel.skipToNextPhase() }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * 设置行组件
 *
 * 显示一个标签、当前值和一个滑块控件
 */
@Composable
private fun SettingRow(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    enabled: Boolean,
    valueRange: IntRange,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = if (label.contains("循环")) "$value 次" else "$value 分钟",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            enabled = enabled,
            steps = valueRange.last - valueRange.first - 1
        )
    }
}
