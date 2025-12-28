package com.azuxa616.focustimer.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object DateUtils {
    fun startOfDayMillis(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): Long {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        return date.atStartOfDay(zoneId).toInstant().toEpochMilli()
    }

    fun endOfDayMillis(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): Long {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
        val nextDay = date.plusDays(1)
        return nextDay.atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
    }

    fun localDateFromMillis(epochMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
        return Instant.ofEpochMilli(epochMillis).atZone(zoneId).toLocalDate()
    }
}

