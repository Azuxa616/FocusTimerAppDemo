package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimerCircle(
    progress: Float,
    timeText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 10.dp,
            modifier = Modifier.matchParentSize()
        )
        Text(
            text = timeText,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

