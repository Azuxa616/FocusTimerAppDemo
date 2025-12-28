package com.azuxa616.focustimer.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId

class DateUtilsTest {
    @Test
    fun startAndEndOfDayAreConsistent() {
        val zoneId = ZoneId.of("UTC")
        val sampleMillis = 1_700_000_000_000L
        val start = DateUtils.startOfDayMillis(sampleMillis, zoneId)
        val end = DateUtils.endOfDayMillis(sampleMillis, zoneId)
        assertEquals(24L * 60L * 60L * 1000L - 1L, end - start)
    }
}

