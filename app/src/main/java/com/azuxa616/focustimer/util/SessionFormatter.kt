package com.azuxa616.focustimer.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 会话格式化工具类
 *
 * 提供专注会话相关的格式化方法
 */
object SessionFormatter {

    /** 日期时间格式化器：yyyy-MM-dd HH:mm */
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /** 日期格式化器：yyyy-MM-dd */
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /** 时间格式化器：HH:mm */
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    /**
     * 格式化时间戳为日期时间字符串
     *
     * @param epochMillis 时间戳（毫秒）
     * @return 格式化后的字符串，如 "2024-01-15 14:30"
     */
    fun formatDateTime(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateTimeFormatter)
    }

    /**
     * 格式化可空时间戳为日期时间字符串
     *
     * @param epochMillis 时间戳（毫秒），可为null
     * @param nullText 当时间戳为null时显示的文本
     * @return 格式化后的字符串
     */
    fun formatDateTimeOrDefault(epochMillis: Long?, nullText: String = "未完成"): String {
        return epochMillis?.let { formatDateTime(it) } ?: nullText
    }

    /**
     * 格式化时间戳为日期字符串
     *
     * @param epochMillis 时间戳（毫秒）
     * @return 格式化后的字符串，如 "2024-01-15"
     */
    fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }

    /**
     * 格式化时间戳为时间字符串
     *
     * @param epochMillis 时间戳（毫秒）
     * @return 格式化后的字符串，如 "14:30"
     */
    fun formatTime(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(timeFormatter)
    }

    /**
     * 格式化专注时长为显示字符串
     *
     * @param minutes 专注时长（分钟）
     * @return 格式化后的字符串，如 "25 分钟"
     */
    fun formatDuration(minutes: Int): String {
        return TimeFormatter.formatMinutesShort(minutes)
    }
}



