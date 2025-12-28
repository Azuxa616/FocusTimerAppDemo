/**
 * 统计状态模块
 *
 * 定义专注统计页面的UI状态数据类
 */
package com.azuxa616.focustimer.ui.screen.statistics

import java.time.LocalDate

// ==================== 枚举定义 ====================

/**
 * 时间范围枚举
 */
enum class TimeRange {
    TODAY,      // 今日
    WEEK,       // 本周
    MONTH,      // 本月
    YEAR,       // 本年
    ALL_TIME    // 全部时间
}

// ==================== 数据类定义 ====================

/**
 * 每日专注汇总
 *
 * 统计某一天的专注数据
 *
 * @param date 日期
 * @param sessionCount 专注次数
 * @param totalMinutes 专注总时长（分钟）
 */
data class DailySummary(
    val date: LocalDate,
    val sessionCount: Int,
    val totalMinutes: Int
)

/**
 * 任务统计数据
 *
 * @param taskId 任务ID
 * @param taskName 任务名称
 * @param sessionCount 专注次数
 * @param totalMinutes 专注总时长（分钟）
 * @param percentage 占比（百分比，0-100）
 */
data class TaskStatistics(
    val taskId: Long,
    val taskName: String,
    val sessionCount: Int,
    val totalMinutes: Int,
    val percentage: Float
)

/**
 * 图表数据点（用于折线图和柱状图）
 *
 * @param label X轴标签
 * @param value Y轴值（分钟）
 */
data class ChartDataPoint(
    val label: String,
    val value: Float
)

/**
 * 成就数据
 *
 * @param consecutiveDays 连续专注天数
 * @param totalTimeMilestone 累计总时长里程碑（分钟）
 * @param nextMilestone 下一个里程碑（分钟）
 */
data class AchievementData(
    val consecutiveDays: Int,
    val totalTimeMilestone: Long,
    val nextMilestone: Long
)

/**
 * 统计页面UI状态
 *
 * @param selectedTimeRange 选中的时间范围
 * @param totalSessionCount 总专注次数
 * @param totalMinutes 总专注时长（分钟）
 * @param averageMinutes 平均每次专注时长（分钟）
 * @param dailySummaries 每日汇总列表（按日期降序）
 * @param taskStatistics 任务统计列表（按时长降序）
 * @param trendChartData 趋势图数据（折线图）
 * @param comparisonChartData 对比图数据（柱状图）
 * @param taskDistributionData 任务分布数据（饼图）
 * @param achievementData 成就数据
 * @param isLoading 是否正在加载
 */
data class StatisticsState(
    val selectedTimeRange: TimeRange = TimeRange.TODAY,
    val totalSessionCount: Int = 0,
    val totalMinutes: Int = 0,
    val averageMinutes: Float = 0f,
    val dailySummaries: List<DailySummary> = emptyList(),
    val taskStatistics: List<TaskStatistics> = emptyList(),
    val trendChartData: List<ChartDataPoint> = emptyList(),
    val comparisonChartData: List<ChartDataPoint> = emptyList(),
    val taskDistributionData: List<ChartDataPoint> = emptyList(),
    val achievementData: AchievementData = AchievementData(0, 0, 1000),
    val isLoading: Boolean = false
) {
    companion object {
        /** 空状态 */
        val Empty = StatisticsState(
            selectedTimeRange = TimeRange.TODAY,
            totalSessionCount = 0,
            totalMinutes = 0,
            averageMinutes = 0f,
            dailySummaries = emptyList(),
            taskStatistics = emptyList(),
            trendChartData = emptyList(),
            comparisonChartData = emptyList(),
            taskDistributionData = emptyList(),
            achievementData = AchievementData(0, 0, 1000),
            isLoading = false
        )
    }
}
