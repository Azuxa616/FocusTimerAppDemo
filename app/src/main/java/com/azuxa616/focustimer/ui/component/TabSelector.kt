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
import com.azuxa616.focustimer.ui.screen.statistics.StatisticsTab

/**
 * Tab选择器
 *
 * 允许用户在"统计"和"记录"两个Tab之间切换
 */
@Composable
fun TabSelector(
    selectedTab: StatisticsTab,
    onTabSelected: (StatisticsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatisticsTab.values().forEach { tab ->
            val isSelected = tab == selectedTab
            Surface(
                modifier = Modifier
                    .toggleable(
                        value = isSelected,
                        role = Role.RadioButton,
                        onValueChange = { if (!isSelected) onTabSelected(tab) }
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
                    text = getTabLabel(tab),
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
 * 获取Tab的显示标签
 */
private fun getTabLabel(tab: StatisticsTab): String {
    return when (tab) {
        StatisticsTab.STATISTICS -> "统计"
        StatisticsTab.RECORDS -> "记录"
    }
}

