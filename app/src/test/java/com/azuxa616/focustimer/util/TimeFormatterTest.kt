package com.azuxa616.focustimer.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatterTest {
    @Test
    fun formatMinutesSeconds_formatsCorrectly() {
        assertEquals("00:00", TimeFormatter.formatMinutesSeconds(0))
        assertEquals("00:05", TimeFormatter.formatMinutesSeconds(5))
        assertEquals("01:00", TimeFormatter.formatMinutesSeconds(60))
        assertEquals("25:00", TimeFormatter.formatMinutesSeconds(25 * 60L))
    }
}

