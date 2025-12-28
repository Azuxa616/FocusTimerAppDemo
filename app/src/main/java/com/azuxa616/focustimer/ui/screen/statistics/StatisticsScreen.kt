package com.azuxa616.focustimer.ui.screen.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.component.AchievementCard
import com.azuxa616.focustimer.ui.component.FocusComparisonChart
import com.azuxa616.focustimer.ui.component.FocusTrendChart
import com.azuxa616.focustimer.ui.component.StatisticsOverviewCard
import com.azuxa616.focustimer.ui.component.TaskDistributionChart
import com.azuxa616.focustimer.ui.component.TaskStatisticsCard
import com.azuxa616.focustimer.ui.component.TimeRangeSelector

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel
) {
    val state by viewModel.state.collectAsState()
    
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // 标题
            Text(
                text = "专注统计",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            
            // 时间范围选择器
            TimeRangeSelector(
                selectedRange = state.selectedTimeRange,
                onRangeSelected = { viewModel.selectTimeRange(it) }
            )
            
            // 加载状态
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                // 统计概览卡片
                StatisticsOverviewCard(
                    totalSessionCount = state.totalSessionCount,
                    totalMinutes = state.totalMinutes,
                    averageMinutes = state.averageMinutes
                )
                
                // 成就卡片
                AchievementCard(achievementData = state.achievementData)
                
                // 趋势图表
                FocusTrendChart(data = state.trendChartData)
                
                // 对比图表
                FocusComparisonChart(data = state.comparisonChartData)
                
                // 任务分布图表
                TaskDistributionChart(data = state.taskDistributionData)
                
                // 任务统计卡片
                TaskStatisticsCard(taskStatistics = state.taskStatistics)
                
                // 底部间距
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

