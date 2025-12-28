package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 统计概览卡片
 *
 * 显示当前时间范围的关键指标
 */
@Composable
fun StatisticsOverviewCard(
    totalSessionCount: Int,
    totalMinutes: Int,
    averageMinutes: Float,
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
                text = "统计概览",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                StatisticItem(
                    label = "专注次数",
                    value = totalSessionCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatisticItem(
                    label = "总时长",
                    value = formatMinutes(totalMinutes),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            StatisticItem(
                label = "平均时长",
                value = formatMinutes(averageMinutes.toInt()),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * 统计项组件
 */
@Composable
private fun StatisticItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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

