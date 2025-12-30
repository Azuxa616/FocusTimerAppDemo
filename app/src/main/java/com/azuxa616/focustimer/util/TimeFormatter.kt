package com.azuxa616.focustimer.util

import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * 时间格式化工具类
 *
 * 提供各种时间格式化方法
 */
object TimeFormatter {

    /**
     * 格式化秒数为 MM:SS 格式
     *
     * @param totalSeconds 总秒数
     * @return 格式化后的字符串，如 "25:00"
     */
    fun formatMinutesSeconds(totalSeconds: Long): String {
        val duration = totalSeconds.toDuration(DurationUnit.SECONDS)
        val minutes = duration.inWholeMinutes
        val seconds = duration.inWholeSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    /**
     * 格式化分钟数为可读的中文字符串
     *
     * 根据时长自动选择合适的单位：
     * - 小于60分钟：显示分钟
     * - 小于24小时：显示小时和分钟
     * - 大于等于24小时：显示天和小时
     *
     * @param minutes 总分钟数
     * @return 格式化后的字符串，如 "25分钟"、"2小时30分钟"、"1天2小时"
     */
    fun formatMinutes(minutes: Int): String {
        return when {
            minutes < 60 -> "${minutes}分钟"
            minutes < 1440 -> {
                val hours = minutes / 60
                val mins = minutes % 60
                if (mins > 0) "${hours}小时${mins}分钟" else "${hours}小时"
            }
            else -> {
                val days = minutes / 1440
                val hours = (minutes % 1440) / 60
                if (hours > 0) "${days}天${hours}小时" else "${days}天"
            }
        }
    }

    /**
     * 格式化分钟数为简短的显示格式
     *
     * @param minutes 总分钟数
     * @return 格式化后的字符串，如 "25 分钟"
     */
    fun formatMinutesShort(minutes: Int): String {
        return "$minutes 分钟"
    }
}
