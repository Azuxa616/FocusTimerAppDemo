package com.azuxa616.focustimer.ui.screen.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.data.repository.FocusRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import com.azuxa616.focustimer.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max

class StatisticsViewModel(
    private val focusRepository: FocusRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val mutableState = MutableStateFlow(StatisticsState.Empty)
    val state: StateFlow<StatisticsState> = mutableState

    init {
        // 监听时间范围变化并更新数据（使用distinctUntilChanged避免重复加载）
        viewModelScope.launch {
            mutableState
                .map { it.selectedTimeRange }
                .distinctUntilChanged()
                .collect { timeRange ->
                    loadStatisticsForTimeRange(timeRange)
                }
        }
    }

    /**
     * 切换时间范围
     */
    fun selectTimeRange(timeRange: TimeRange) {
        mutableState.value = mutableState.value.copy(
            selectedTimeRange = timeRange,
            isLoading = true
        )
    }

    /**
     * 加载指定时间范围的统计数据
     */
    private fun loadStatisticsForTimeRange(timeRange: TimeRange) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val (startMillis, endMillis) = getTimeRangeBounds(now, timeRange)
            
            // 获取会话和任务数据
            val sessionsFlow = focusRepository.sessionsBetween(startMillis, endMillis)
            val tasksFlow = taskRepository.allTasks
            
            combine(sessionsFlow, tasksFlow) { sessions, tasks ->
                // 只统计已完成的会话
                val completedSessions = sessions.filter { it.endTimeMillis != null }
                
                // 创建任务ID到任务名称的映射
                val taskMap = tasks.associateBy { it.id }
                
                // 计算基础统计
                val totalSessionCount = completedSessions.size
                val totalMinutes = completedSessions.sumOf { it.actualTotalMinutes }
                val averageMinutes = if (totalSessionCount > 0) {
                    totalMinutes.toFloat() / totalSessionCount
                } else {
                    0f
                }
                
                // 计算每日汇总
                val dailySummaries = calculateDailySummaries(completedSessions, timeRange)
                
                // 计算任务统计
                val taskStatistics = calculateTaskStatistics(completedSessions, taskMap, totalMinutes)
                
                // 计算图表数据
                val trendChartData = calculateTrendChartData(dailySummaries, timeRange)
                val comparisonChartData = calculateComparisonChartData(dailySummaries, timeRange)
                val taskDistributionData = calculateTaskDistributionData(taskStatistics)
                
                // 计算成就数据
                val achievementData = calculateAchievementData(now)
                
                mutableState.value = StatisticsState(
                    selectedTimeRange = timeRange,
                    totalSessionCount = totalSessionCount,
                    totalMinutes = totalMinutes,
                    averageMinutes = averageMinutes,
                    dailySummaries = dailySummaries,
                    taskStatistics = taskStatistics,
                    trendChartData = trendChartData,
                    comparisonChartData = comparisonChartData,
                    taskDistributionData = taskDistributionData,
                    achievementData = achievementData,
                    isLoading = false
                )
            }.collect { }
        }
    }

    /**
     * 获取时间范围的边界
     */
    private fun getTimeRangeBounds(now: Long, timeRange: TimeRange): Pair<Long, Long> {
        val endMillis = DateUtils.endOfDayMillis(now)
        val startMillis = when (timeRange) {
            TimeRange.TODAY -> DateUtils.startOfDayMillis(now)
            TimeRange.WEEK -> {
                val today = DateUtils.localDateFromMillis(now)
                val weekStart = today.minusDays((today.dayOfWeek.value - 1).toLong())
                DateUtils.startOfDayMillis(
                    weekStart.atStartOfDay(java.time.ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                )
            }
            TimeRange.MONTH -> {
                val today = DateUtils.localDateFromMillis(now)
                val monthStart = today.withDayOfMonth(1)
                DateUtils.startOfDayMillis(
                    monthStart.atStartOfDay(java.time.ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                )
            }
            TimeRange.YEAR -> {
                val today = DateUtils.localDateFromMillis(now)
                val yearStart = today.withDayOfYear(1)
                DateUtils.startOfDayMillis(
                    yearStart.atStartOfDay(java.time.ZoneId.systemDefault())
                        .toInstant().toEpochMilli()
                )
            }
            TimeRange.ALL_TIME -> 0L
        }
        return Pair(startMillis, endMillis)
    }

    /**
     * 计算每日汇总
     */
    private fun calculateDailySummaries(
        sessions: List<FocusSession>,
        timeRange: TimeRange
    ): List<DailySummary> {
        val byDate = sessions.groupBy { session ->
            DateUtils.localDateFromMillis(session.startTimeMillis)
        }
        
        return byDate.entries
            .sortedByDescending { it.key }
            .map { (date, list) ->
                DailySummary(
                    date = date,
                    sessionCount = list.size,
                    totalMinutes = list.sumOf { it.actualTotalMinutes }
                )
            }
    }

    /**
     * 计算任务统计
     */
    private fun calculateTaskStatistics(
        sessions: List<FocusSession>,
        taskMap: Map<Long, com.azuxa616.focustimer.data.model.Task>,
        totalMinutes: Int
    ): List<TaskStatistics> {
        val byTask = sessions.groupBy { it.taskId }
        
        return byTask.entries.map { (taskId, taskSessions) ->
            val task = taskMap[taskId]
            val taskMinutes = taskSessions.sumOf { it.actualTotalMinutes }
            val percentage = if (totalMinutes > 0) {
                (taskMinutes.toFloat() / totalMinutes * 100f).coerceIn(0f, 100f)
            } else {
                0f
            }
            
            TaskStatistics(
                taskId = taskId,
                taskName = task?.name ?: "未知任务",
                sessionCount = taskSessions.size,
                totalMinutes = taskMinutes,
                percentage = percentage
            )
        }.sortedByDescending { it.totalMinutes }
    }

    /**
     * 计算趋势图数据（折线图）
     */
    private fun calculateTrendChartData(
        dailySummaries: List<DailySummary>,
        timeRange: TimeRange
    ): List<ChartDataPoint> {
        val formatter = when (timeRange) {
            TimeRange.TODAY, TimeRange.WEEK -> DateTimeFormatter.ofPattern("MM-dd")
            TimeRange.MONTH -> DateTimeFormatter.ofPattern("dd")
            TimeRange.YEAR -> DateTimeFormatter.ofPattern("MM")
            TimeRange.ALL_TIME -> DateTimeFormatter.ofPattern("yyyy-MM")
        }
        
        return dailySummaries.take(30).map { summary ->
            ChartDataPoint(
                label = summary.date.format(formatter),
                value = summary.totalMinutes.toFloat()
            )
        }.reversed()
    }

    /**
     * 计算对比图数据（柱状图）
     */
    private fun calculateComparisonChartData(
        dailySummaries: List<DailySummary>,
        timeRange: TimeRange
    ): List<ChartDataPoint> {
        val formatter = when (timeRange) {
            TimeRange.TODAY, TimeRange.WEEK -> DateTimeFormatter.ofPattern("MM-dd")
            TimeRange.MONTH -> DateTimeFormatter.ofPattern("dd")
            TimeRange.YEAR -> DateTimeFormatter.ofPattern("MM")
            TimeRange.ALL_TIME -> DateTimeFormatter.ofPattern("yyyy-MM")
        }
        
        return dailySummaries.take(14).map { summary ->
            ChartDataPoint(
                label = summary.date.format(formatter),
                value = summary.totalMinutes.toFloat()
            )
        }.reversed()
    }

    /**
     * 计算任务分布数据（饼图）
     */
    private fun calculateTaskDistributionData(
        taskStatistics: List<TaskStatistics>
    ): List<ChartDataPoint> {
        return taskStatistics.take(10).map { task ->
            ChartDataPoint(
                label = task.taskName,
                value = task.percentage
            )
        }
    }

    /**
     * 计算成就数据
     */
    private suspend fun calculateAchievementData(now: Long): AchievementData {
        // 计算连续专注天数
        val consecutiveDays = calculateConsecutiveDays(now)
        
        // 计算累计总时长
        val allSessions = focusRepository.getAllSessions().first()
        val completedSessions = allSessions.filter { it.endTimeMillis != null }
        val totalTimeMilestone = completedSessions.sumOf { it.actualTotalMinutes.toLong() }
        
        // 计算下一个里程碑（1000分钟、5000分钟、10000分钟等）
        val milestones = listOf(1000L, 5000L, 10000L, 25000L, 50000L, 100000L)
        val nextMilestone = milestones.firstOrNull { it > totalTimeMilestone } ?: milestones.last()
        
        return AchievementData(
            consecutiveDays = consecutiveDays,
            totalTimeMilestone = totalTimeMilestone,
            nextMilestone = nextMilestone
        )
    }

    /**
     * 计算连续专注天数
     */
    private suspend fun calculateConsecutiveDays(now: Long): Int {
        val allSessions = focusRepository.getAllSessions().first()
        val completedSessions = allSessions
            .filter { it.endTimeMillis != null }
            .map { DateUtils.localDateFromMillis(it.startTimeMillis) }
            .distinct()
            .sortedDescending()
        
        if (completedSessions.isEmpty()) return 0
        
        val today = DateUtils.localDateFromMillis(now)
        var consecutiveDays = 0
        var expectedDate = today
        
        for (date in completedSessions) {
            if (date == expectedDate || date == expectedDate.minusDays(1)) {
                consecutiveDays++
                expectedDate = date.minusDays(1)
            } else {
                break
            }
        }
        
        return consecutiveDays
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

