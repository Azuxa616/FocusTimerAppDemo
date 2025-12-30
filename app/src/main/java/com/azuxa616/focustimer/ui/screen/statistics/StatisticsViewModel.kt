package com.azuxa616.focustimer.ui.screen.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.data.repository.FocusRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import com.azuxa616.focustimer.util.StatisticsCalculator
import com.azuxa616.focustimer.util.TimeRangeCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 统计页面ViewModel
 *
 * 负责管理统计页面的UI状态和数据加载逻辑
 * 计算逻辑委托给StatisticsCalculator处理
 */
class StatisticsViewModel(
    private val focusRepository: FocusRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val mutableState = MutableStateFlow(StatisticsState.Empty)
    val state: StateFlow<StatisticsState> = mutableState

    init {
        // 初始化时加载统计Tab的数据
        viewModelScope.launch {
            loadStatisticsForTimeRange(TimeRange.ALL_TIME)
        }

        // 监听Tab变化并更新数据
        viewModelScope.launch {
            mutableState
                .map { it.selectedTab }
                .distinctUntilChanged()
                .collect { tab ->
                    when (tab) {
                        StatisticsTab.STATISTICS -> {
                            loadStatisticsForTimeRange(mutableState.value.selectedTimeRange)
                        }
                        StatisticsTab.RECORDS -> {
                            loadFocusSessions()
                        }
                    }
                }
        }
    }

    /**
     * 切换Tab
     */
    fun selectTab(tab: StatisticsTab) {
        mutableState.value = mutableState.value.copy(
            selectedTab = tab,
            isLoading = true
        )
    }

    /**
     * 切换时间范围（仅用于统计Tab）
     */
    fun selectTimeRange(timeRange: TimeRange) {
        mutableState.value = mutableState.value.copy(
            selectedTimeRange = timeRange,
            isLoading = true
        )
        if (mutableState.value.selectedTab == StatisticsTab.STATISTICS) {
            loadStatisticsForTimeRange(timeRange)
        }
    }

    /**
     * 删除专注会话记录
     */
    fun deleteSession(session: FocusSession) {
        viewModelScope.launch {
            focusRepository.deleteFocusSession(session)
        }
    }

    /**
     * 加载专注历史记录
     */
    private fun loadFocusSessions() {
        viewModelScope.launch {
            combine(focusRepository.getAllSessions(), taskRepository.allTasks) { sessions, tasks ->
                // 只显示已完成的会话，按时间倒序排列
                val completedSessions = sessions
                    .filter { it.endTimeMillis != null }
                    .sortedByDescending { it.startTimeMillis }

                // 创建任务ID到任务的映射
                val taskMap = tasks.associateBy { it.id }

                Pair(completedSessions, taskMap)
            }.collect { (sessions, taskMap) ->
                mutableState.value = mutableState.value.copy(
                    focusSessions = sessions,
                    taskMap = taskMap,
                    isLoading = false
                )
            }
        }
    }

    /**
     * 加载指定时间范围的统计数据
     */
    private fun loadStatisticsForTimeRange(timeRange: TimeRange) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val (startMillis, endMillis) = TimeRangeCalculator.getTimeRangeBounds(now, timeRange)

            // 获取会话和任务数据
            val sessionsFlow = focusRepository.sessionsBetween(startMillis, endMillis)
            val tasksFlow = taskRepository.allTasks

            combine(sessionsFlow, tasksFlow) { sessions, tasks ->
                // 只统计已完成的会话
                val completedSessions = sessions.filter { it.endTimeMillis != null }

                // 创建任务ID到任务的映射
                val taskMap = tasks.associateBy { it.id }

                // 使用StatisticsCalculator计算统计数据
                val (totalSessionCount, totalMinutes, averageMinutes) =
                    StatisticsCalculator.calculateBasicStatistics(completedSessions)

                val dailySummaries = StatisticsCalculator.calculateDailySummaries(completedSessions)
                val taskStatistics = StatisticsCalculator.calculateTaskStatistics(
                    completedSessions, taskMap, totalMinutes
                )

                val trendChartData = StatisticsCalculator.calculateTrendChartData(dailySummaries, timeRange)
                val comparisonChartData = StatisticsCalculator.calculateComparisonChartData(dailySummaries, timeRange)
                val taskDistributionData = StatisticsCalculator.calculateTaskDistributionData(taskStatistics)

                StatisticsData(
                    totalSessionCount = totalSessionCount,
                    totalMinutes = totalMinutes,
                    averageMinutes = averageMinutes,
                    dailySummaries = dailySummaries,
                    taskStatistics = taskStatistics,
                    trendChartData = trendChartData,
                    comparisonChartData = comparisonChartData,
                    taskDistributionData = taskDistributionData
                )
            }.collect { statisticsData ->
                // 计算成就数据
                val allSessions = focusRepository.getAllSessions().first()
                val achievementData = StatisticsCalculator.calculateAchievementData(allSessions, now)

                mutableState.value = mutableState.value.copy(
                    selectedTimeRange = timeRange,
                    totalSessionCount = statisticsData.totalSessionCount,
                    totalMinutes = statisticsData.totalMinutes,
                    averageMinutes = statisticsData.averageMinutes,
                    dailySummaries = statisticsData.dailySummaries,
                    taskStatistics = statisticsData.taskStatistics,
                    trendChartData = statisticsData.trendChartData,
                    comparisonChartData = statisticsData.comparisonChartData,
                    taskDistributionData = statisticsData.taskDistributionData,
                    achievementData = achievementData,
                    isLoading = false
                )
            }
        }
    }

    companion object {
        class Factory(
            private val focusRepository: FocusRepository,
            private val taskRepository: TaskRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return StatisticsViewModel(focusRepository, taskRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}

/**
 * 统计数据临时数据类（用于在combine中传递数据）
 */
private data class StatisticsData(
    val totalSessionCount: Int,
    val totalMinutes: Int,
    val averageMinutes: Float,
    val dailySummaries: List<DailySummary>,
    val taskStatistics: List<TaskStatistics>,
    val trendChartData: List<ChartDataPoint>,
    val comparisonChartData: List<ChartDataPoint>,
    val taskDistributionData: List<ChartDataPoint>
)
