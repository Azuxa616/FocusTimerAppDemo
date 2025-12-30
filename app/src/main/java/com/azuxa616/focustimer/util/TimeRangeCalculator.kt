package com.azuxa616.focustimer.util

import com.azuxa616.focustimer.ui.screen.statistics.TimeRange
import java.time.ZoneId

/**
 * 时间范围计算工具类
 *
 * 用于计算各种时间范围的起止时间戳
 */
object TimeRangeCalculator {

    /**
     * 获取指定时间范围的起止时间戳
     *
     * @param currentTimeMillis 当前时间戳（毫秒）
     * @param timeRange 时间范围类型
     * @return Pair<开始时间戳, 结束时间戳>
     */
    fun getTimeRangeBounds(currentTimeMillis: Long, timeRange: TimeRange): Pair<Long, Long> {
        val endMillis = DateUtils.endOfDayMillis(currentTimeMillis)
        val startMillis = when (timeRange) {
            TimeRange.TODAY -> DateUtils.startOfDayMillis(currentTimeMillis)
            TimeRange.WEEK -> calculateWeekStart(currentTimeMillis)
            TimeRange.MONTH -> calculateMonthStart(currentTimeMillis)
            TimeRange.YEAR -> calculateYearStart(currentTimeMillis)
            TimeRange.ALL_TIME -> 0L
        }
        return Pair(startMillis, endMillis)
    }

    /**
     * 计算本周开始时间戳
     */
    private fun calculateWeekStart(currentTimeMillis: Long): Long {
        val today = DateUtils.localDateFromMillis(currentTimeMillis)
        val weekStart = today.minusDays((today.dayOfWeek.value - 1).toLong())
        return DateUtils.startOfDayMillis(
            weekStart.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
    }

    /**
     * 计算本月开始时间戳
     */
    private fun calculateMonthStart(currentTimeMillis: Long): Long {
        val today = DateUtils.localDateFromMillis(currentTimeMillis)
        val monthStart = today.withDayOfMonth(1)
        return DateUtils.startOfDayMillis(
            monthStart.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
    }

    /**
     * 计算本年开始时间戳
     */
    private fun calculateYearStart(currentTimeMillis: Long): Long {
        val today = DateUtils.localDateFromMillis(currentTimeMillis)
        val yearStart = today.withDayOfYear(1)
        return DateUtils.startOfDayMillis(
            yearStart.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
    }
}



