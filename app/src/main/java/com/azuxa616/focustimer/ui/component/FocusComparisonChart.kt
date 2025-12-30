package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.ChartDataPoint

/**
 * 专注对比图表（柱状图）
 *
 * 显示不同时间段的专注时长对比
 */
@Composable
fun FocusComparisonChart(
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
                text = "专注对比",
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
                // 使用Canvas绘制简单的柱状图
                val maxValue = data.maxOfOrNull { it.value } ?: 1f
                val minValue = data.minOfOrNull { it.value } ?: 0f
                val valueRange = (maxValue - minValue).coerceAtLeast(1f)
                
                // 在Canvas外部获取颜色
                val gridColor = MaterialTheme.colorScheme.outlineVariant
                val primaryColor = MaterialTheme.colorScheme.primary
                
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                ) {
                    val padding = 40f
                    val chartWidth = size.width - padding * 2
                    val chartHeight = size.height - padding * 2
                    val columnCount = data.size
                    val columnWidth = (chartWidth / columnCount) * 0.6f
                    val spacing = (chartWidth / columnCount) * 0.4f
                    
                    // 绘制网格线
                    for (i in 0..4) {
                        val y = padding + (chartHeight / 4) * i
                        drawLine(
                            color = gridColor,
                            start = Offset(padding, y),
                            end = Offset(size.width - padding, y),
                            strokeWidth = 1f
                        )
                    }
                    
                    // 绘制柱子
                    data.forEachIndexed { index, point ->
                        val normalizedValue = (point.value - minValue) / valueRange
                        val columnHeight = normalizedValue * chartHeight
                        val x = padding + spacing / 2 + (columnWidth + spacing) * index
                        val y = padding + chartHeight - columnHeight
                        
                        drawRect(
                            color = primaryColor,
                            topLeft = Offset(x, y),
                            size = Size(columnWidth, columnHeight)
                        )
                    }
                }
            }
        }
    }
}

