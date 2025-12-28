package com.azuxa616.focustimer.util

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeFormatter {
    fun formatMinutesSeconds(totalSeconds: Long): String {
        val duration = totalSeconds.toDuration(DurationUnit.SECONDS)
        val minutes = duration.inWholeMinutes
        val seconds = duration.inWholeSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}

