/**
 * 统计状态模块
 *
 * 定义专注统计页面的UI状态数据类
 */
package com.azuxa616.focustimer.ui.screen.statistics

import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.data.model.Task
import java.time.LocalDate

// ==================== 枚举定义 ====================

/**
 * 统计页面Tab枚举
 */
enum class StatisticsTab {
    STATISTICS,  // 统计
    RECORDS      // 记录
}

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
 * @param selectedTab 选中的Tab（统计/记录）
 * @param selectedTimeRange 选中的时间范围（仅用于统计Tab，默认ALL_TIME）
 * @param totalSessionCount 总专注次数
 * @param totalMinutes 总专注时长（分钟）
 * @param averageMinutes 平均每次专注时长（分钟）
 * @param dailySummaries 每日汇总列表（按日期降序）
 * @param taskStatistics 任务统计列表（按时长降序）
 * @param trendChartData 趋势图数据（折线图）
 * @param comparisonChartData 对比图数据（柱状图）
 * @param taskDistributionData 任务分布数据（饼图）
 * @param achievementData 成就数据
 * @param focusSessions 专注历史记录列表（按时间倒序）
 * @param taskMap 任务ID到任务名称的映射
 * @param isLoading 是否正在加载
 */
data class StatisticsState(
    val selectedTab: StatisticsTab = StatisticsTab.STATISTICS,
    val selectedTimeRange: TimeRange = TimeRange.ALL_TIME,
    val totalSessionCount: Int = 0,
    val totalMinutes: Int = 0,
    val averageMinutes: Float = 0f,
    val dailySummaries: List<DailySummary> = emptyList(),
    val taskStatistics: List<TaskStatistics> = emptyList(),
    val trendChartData: List<ChartDataPoint> = emptyList(),
    val comparisonChartData: List<ChartDataPoint> = emptyList(),
    val taskDistributionData: List<ChartDataPoint> = emptyList(),
    val achievementData: AchievementData = AchievementData(0, 0, 1000),
    val focusSessions: List<FocusSession> = emptyList(),
    val taskMap: Map<Long, Task> = emptyMap(),
    val isLoading: Boolean = false
) {
    companion object {
        /** 空状态 */
        val Empty = StatisticsState(
            selectedTab = StatisticsTab.STATISTICS,
            selectedTimeRange = TimeRange.ALL_TIME,
            totalSessionCount = 0,
            totalMinutes = 0,
            averageMinutes = 0f,
            dailySummaries = emptyList(),
            taskStatistics = emptyList(),
            trendChartData = emptyList(),
            comparisonChartData = emptyList(),
            taskDistributionData = emptyList(),
            achievementData = AchievementData(0, 0, 1000),
            focusSessions = emptyList(),
            taskMap = emptyMap(),
            isLoading = false
        )
    }
}
