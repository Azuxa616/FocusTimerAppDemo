package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.ChartDataPoint

/**
 * 任务分布图表（饼图）
 *
 * 显示不同任务的专注时长分布占比
 */
@Composable
fun TaskDistributionChart(
    data: List<ChartDataPoint>,
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
                text = "任务分布",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (data.isEmpty()) {
                Text(
                    text = "暂无数据",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )
            } else {
                // 生成颜色列表
                val colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.tertiaryContainer
                )
                
                // 计算总和
                val total = data.sumOf { it.value.toDouble() }.toFloat()
                
                // 使用Canvas绘制饼图
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = (size.width.coerceAtMost(size.height) / 2) * 0.7f
                    var startAngle = -90f
                    
                    data.forEachIndexed { index, point ->
                        val sweepAngle = (point.value / total) * 360f
                        val color = colors[index % colors.size]
                        
                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2)
                        )
                        
                        startAngle += sweepAngle
                    }
                }
                
                // 显示图例
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    data.take(6).forEachIndexed { index, point ->
                        RowWithLegend(
                            label = point.label,
                            percentage = point.value,
                            color = colors[index % colors.size]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowWithLegend(
    label: String,
    percentage: Float,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(16.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

