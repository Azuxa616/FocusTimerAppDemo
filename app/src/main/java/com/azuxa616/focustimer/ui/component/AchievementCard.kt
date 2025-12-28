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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.AchievementData

/**
 * 成就卡片
 *
 * 显示连续专注天数和累计总时长里程碑
 */
@Composable
fun AchievementCard(
    achievementData: AchievementData,
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
                text = "成就",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // 连续专注天数
            AchievementItem(
                label = "连续专注",
                value = "${achievementData.consecutiveDays}天",
                icon = "🔥"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 累计总时长里程碑
            val progress = if (achievementData.nextMilestone > 0) {
                (achievementData.totalTimeMilestone.toFloat() / achievementData.nextMilestone).coerceIn(0f, 1f)
            } else {
                1f
            }
            
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "累计专注",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatMinutes(achievementData.totalTimeMilestone.toInt()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "下一个里程碑: ${formatMinutes(achievementData.nextMilestone.toInt())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 成就项组件
 */
@Composable
private fun AchievementItem(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
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

