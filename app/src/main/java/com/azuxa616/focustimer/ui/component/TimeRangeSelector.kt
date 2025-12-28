package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.TimeRange

/**
 * 时间范围选择器
 *
 * 允许用户在不同时间范围之间切换
 */
@Composable
fun TimeRangeSelector(
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimeRange.values().forEach { range ->
            val isSelected = range == selectedRange
            Surface(
                modifier = Modifier
                    .toggleable(
                        value = isSelected,
                        role = Role.RadioButton,
                        onValueChange = { if (!isSelected) onRangeSelected(range) }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                shape = MaterialTheme.shapes.small,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Text(
                    text = getTimeRangeLabel(range),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * 获取时间范围的显示标签
 */
private fun getTimeRangeLabel(range: TimeRange): String {
    return when (range) {
        TimeRange.TODAY -> "今日"
        TimeRange.WEEK -> "本周"
        TimeRange.MONTH -> "本月"
        TimeRange.YEAR -> "本年"
        TimeRange.ALL_TIME -> "全部"
    }
}

