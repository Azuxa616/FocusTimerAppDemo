package com.azuxa616.focustimer.ui.screen.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.azuxa616.focustimer.ui.component.FocusSessionItem
import com.azuxa616.focustimer.ui.component.StatisticsOverviewCard
import com.azuxa616.focustimer.ui.component.TabSelector
import com.azuxa616.focustimer.ui.component.TaskDistributionChart
import com.azuxa616.focustimer.ui.component.TaskStatisticsCard

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel
) {
    val state by viewModel.state.collectAsState()
    
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 标题
            Text(
                text = "专注统计",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            
            // Tab选择器
            TabSelector(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
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
                // 根据选中的Tab显示不同内容
                when (state.selectedTab) {
                    StatisticsTab.STATISTICS -> {
                        // 统计Tab：显示统计图表和卡片
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // 统计概览卡片
                            StatisticsOverviewCard(
                                totalSessionCount = state.totalSessionCount,
                                totalMinutes = state.totalMinutes,
                                averageMinutes = state.averageMinutes
                            )
                            
                            // 成就卡片
                            AchievementCard(achievementData = state.achievementData)
                            
                            // 任务分布图表
                            TaskDistributionChart(data = state.taskDistributionData)
                            
                            // 任务统计卡片
                            TaskStatisticsCard(taskStatistics = state.taskStatistics)
                            
                            // 底部间距
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    StatisticsTab.RECORDS -> {
                        // 记录Tab：显示历史记录列表
                        if (state.focusSessions.isEmpty()) {
                            // 空状态
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "暂无历史记录",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            // 历史记录列表
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                items(state.focusSessions) { session ->
                                    val taskName = state.taskMap[session.taskId]?.name ?: "未知任务"
                                    FocusSessionItem(
                                        session = session,
                                        taskName = taskName,
                                        onDelete = { viewModel.deleteSession(session) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

