package com.azuxa616.focustimer.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.util.SessionFormatter

/**
 * 专注会话历史记录项
 *
 * 可展开/折叠的卡片组件，显示专注会话的详细信息
 *
 * @param session 专注会话数据
 * @param taskName 任务名称
 * @param modifier 修饰符
 */
@Composable
fun FocusSessionItem(
    session: FocusSession,
    taskName: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // 使用SessionFormatter格式化时间
    val startTime = SessionFormatter.formatDateTime(session.startTimeMillis)
    val endTime = SessionFormatter.formatDateTimeOrDefault(session.endTimeMillis)
    val totalDurationText = SessionFormatter.formatDuration(session.actualTotalMinutes)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 折叠时的内容：专注总时长（强调）+ 任务名称
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 专注总时长（强调样式）
                Text(
                    text = totalDurationText,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                // 任务名称
                Text(
                    text = taskName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )

                // 展开/折叠按钮
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "折叠" else "展开"
                    )
                }
            }

            // 展开时的内容：开始时间、结束时间、循环次数
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    // 开始时间
                    SessionDetailRow(label = "开始时间：", value = startTime)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 结束时间
                    SessionDetailRow(label = "结束时间：", value = endTime)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 循环次数
                    SessionDetailRow(label = "循环次数：", value = "${session.cycles}")
                }
            }
        }
    }
}

/**
 * 会话详情行组件
 */
@Composable
private fun SessionDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
