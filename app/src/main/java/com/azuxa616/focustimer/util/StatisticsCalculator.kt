package com.azuxa616.focustimer.util

import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.ui.screen.statistics.AchievementData
import com.azuxa616.focustimer.ui.screen.statistics.ChartDataPoint
import com.azuxa616.focustimer.ui.screen.statistics.DailySummary
import com.azuxa616.focustimer.ui.screen.statistics.TaskStatistics
import com.azuxa616.focustimer.ui.screen.statistics.TimeRange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 统计计算工具类
 *
 * 封装所有统计数据的计算逻辑，包括：
 * - 每日汇总计算
 * - 任务统计计算
 * - 图表数据计算
 * - 成就数据计算
 */
object StatisticsCalculator {

    // ==================== 每日汇总计算 ====================

    /**
     * 计算每日专注汇总
     *
     * @param sessions 已完成的专注会话列表
     * @return 按日期降序排列的每日汇总列表
     */
    fun calculateDailySummaries(sessions: List<FocusSession>): List<DailySummary> {
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

    // ==================== 任务统计计算 ====================

    /**
     * 计算任务统计数据
     *
     * @param sessions 已完成的专注会话列表
     * @param taskMap 任务ID到任务对象的映射
     * @param totalMinutes 总专注时长（用于计算百分比）
     * @return 按时长降序排列的任务统计列表
     */
    fun calculateTaskStatistics(
        sessions: List<FocusSession>,
        taskMap: Map<Long, Task>,
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

    // ==================== 图表数据计算 ====================

    /**
     * 获取时间范围对应的日期格式化器
     */
    private fun getDateFormatterForTimeRange(timeRange: TimeRange): DateTimeFormatter {
        return when (timeRange) {
            TimeRange.TODAY, TimeRange.WEEK -> DateTimeFormatter.ofPattern("MM-dd")
            TimeRange.MONTH -> DateTimeFormatter.ofPattern("dd")
            TimeRange.YEAR -> DateTimeFormatter.ofPattern("MM")
            TimeRange.ALL_TIME -> DateTimeFormatter.ofPattern("yyyy-MM")
        }
    }

    /**
     * 计算趋势图数据（折线图）
     *
     * @param dailySummaries 每日汇总列表
     * @param timeRange 时间范围（用于确定日期格式）
     * @return 图表数据点列表（最多30条，按时间正序）
     */
    fun calculateTrendChartData(
        dailySummaries: List<DailySummary>,
        timeRange: TimeRange
    ): List<ChartDataPoint> {
        val formatter = getDateFormatterForTimeRange(timeRange)

        return dailySummaries.take(30).map { summary ->
            ChartDataPoint(
                label = summary.date.format(formatter),
                value = summary.totalMinutes.toFloat()
            )
        }.reversed()
    }

    /**
     * 计算对比图数据（柱状图）
     *
     * @param dailySummaries 每日汇总列表
     * @param timeRange 时间范围（用于确定日期格式）
     * @return 图表数据点列表（最多14条，按时间正序）
     */
    fun calculateComparisonChartData(
        dailySummaries: List<DailySummary>,
        timeRange: TimeRange
    ): List<ChartDataPoint> {
        val formatter = getDateFormatterForTimeRange(timeRange)

        return dailySummaries.take(14).map { summary ->
            ChartDataPoint(
                label = summary.date.format(formatter),
                value = summary.totalMinutes.toFloat()
            )
        }.reversed()
    }

    /**
     * 计算任务分布数据（饼图）
     *
     * @param taskStatistics 任务统计列表
     * @return 图表数据点列表（最多10个任务）
     */
    fun calculateTaskDistributionData(taskStatistics: List<TaskStatistics>): List<ChartDataPoint> {
        return taskStatistics.take(10).map { task ->
            ChartDataPoint(
                label = task.taskName,
                value = task.percentage
            )
        }
    }

    // ==================== 成就数据计算 ====================

    /**
     * 计算成就数据
     *
     * @param allSessions 所有专注会话列表
     * @param currentTimeMillis 当前时间戳
     * @return 成就数据
     */
    fun calculateAchievementData(
        allSessions: List<FocusSession>,
        currentTimeMillis: Long
    ): AchievementData {
        val completedSessions = allSessions.filter { it.endTimeMillis != null }

        // 计算连续专注天数
        val consecutiveDays = calculateConsecutiveDays(completedSessions, currentTimeMillis)

        // 计算累计总时长
        val totalTimeMilestone = completedSessions.sumOf { it.actualTotalMinutes.toLong() }

        // 计算下一个里程碑
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
     *
     * @param completedSessions 已完成的专注会话列表
     * @param currentTimeMillis 当前时间戳
     * @return 连续专注天数
     */
    fun calculateConsecutiveDays(
        completedSessions: List<FocusSession>,
        currentTimeMillis: Long
    ): Int {
        val sessionDates = completedSessions
            .map { DateUtils.localDateFromMillis(it.startTimeMillis) }
            .distinct()
            .sortedDescending()

        if (sessionDates.isEmpty()) return 0

        val today = DateUtils.localDateFromMillis(currentTimeMillis)
        var consecutiveDays = 0
        var expectedDate = today

        for (date in sessionDates) {
            if (date == expectedDate || date == expectedDate.minusDays(1)) {
                consecutiveDays++
                expectedDate = date.minusDays(1)
            } else {
                break
            }
        }

        return consecutiveDays
    }

    // ==================== 基础统计计算 ====================

    /**
     * 计算基础统计数据
     *
     * @param sessions 已完成的专注会话列表
     * @return Triple<总次数, 总时长, 平均时长>
     */
    fun calculateBasicStatistics(sessions: List<FocusSession>): Triple<Int, Int, Float> {
        val totalSessionCount = sessions.size
        val totalMinutes = sessions.sumOf { it.actualTotalMinutes }
        val averageMinutes = if (totalSessionCount > 0) {
            totalMinutes.toFloat() / totalSessionCount
        } else {
            0f
        }
        return Triple(totalSessionCount, totalMinutes, averageMinutes)
    }
}



