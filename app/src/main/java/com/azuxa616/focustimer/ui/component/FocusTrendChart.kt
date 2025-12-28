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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.ChartDataPoint

/**
 * 专注趋势图表（折线图）
 *
 * 显示专注时长的趋势变化
 */
@Composable
fun FocusTrendChart(
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
                text = "专注趋势",
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
                // 使用Canvas绘制简单的折线图
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
                    
                    // 绘制折线
                    val path = Path()
                    val pointSize = data.size
                    val stepX = chartWidth / (pointSize - 1).coerceAtLeast(1)
                    
                    data.forEachIndexed { index, point ->
                        val x = padding + stepX * index
                        val normalizedValue = (point.value - minValue) / valueRange
                        val y = padding + chartHeight - (normalizedValue * chartHeight)
                        
                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }
                    
                    drawPath(
                        path = path,
                        color = primaryColor,
                        style = Stroke(width = 3f)
                    )
                    
                    // 绘制数据点
                    data.forEachIndexed { index, point ->
                        val x = padding + stepX * index
                        val normalizedValue = (point.value - minValue) / valueRange
                        val y = padding + chartHeight - (normalizedValue * chartHeight)
                        
                        drawCircle(
                            color = primaryColor,
                            radius = 4f,
                            center = Offset(x, y)
                        )
                    }
                }
            }
        }
    }
}

