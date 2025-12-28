package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azuxa616.focustimer.ui.screen.statistics.DailySummary
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd")

@Composable
fun DailySummaryCard(
    summary: DailySummary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = summary.date.format(dateFormatter),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "专注次数：${summary.sessionCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "专注总时长：${summary.totalMinutes} 分钟",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

