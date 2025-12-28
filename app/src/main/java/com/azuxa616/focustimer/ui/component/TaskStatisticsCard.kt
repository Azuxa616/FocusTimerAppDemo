package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.TaskStatistics

/**
 * 任务统计卡片
 *
 * 显示每个任务的详细统计信息
 */
@Composable
fun TaskStatisticsCard(
    taskStatistics: List<TaskStatistics>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "任务统计",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (taskStatistics.isEmpty()) {
                Text(
                    text = "暂无数据",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                ) {
                    taskStatistics.forEach { task ->
                        TaskStatisticsItem(task = task)
                    }
                }
            }
        }
    }
}

/**
 * 任务统计项组件
 */
@Composable
private fun TaskStatisticsItem(
    task: TaskStatistics,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.taskName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${task.sessionCount}次 · ${formatMinutes(task.totalMinutes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${task.percentage.toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = task.percentage / 100f,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * 格式化分钟数为可读字符串
 */
private fun formatMinutes(minutes: Int): String {
    return when {
        minutes < 60 -> "${minutes}分钟"
        minutes < 1440 -> {
            val hours = minutes / 60
            val mins = minutes % 60
            if (mins > 0) "${hours}小时${mins}分钟" else "${hours}小时"
        }
        else -> {
            val days = minutes / 1440
            val hours = (minutes % 1440) / 60
            if (hours > 0) "${days}天${hours}小时" else "${days}天"
        }
    }
}

