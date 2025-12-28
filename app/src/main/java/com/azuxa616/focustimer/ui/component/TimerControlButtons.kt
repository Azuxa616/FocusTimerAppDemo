package com.azuxa616.focustimer.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 计时器控制按钮组件
 *
 * 提供三个图标按钮：
 * - 暂停/继续：切换计时器运行状态
 * - 停止：停止计时并保存会话
 * - 跳至下一阶段：跳过当前阶段
 *
 * @param isRunning 计时器是否正在运行
 * @param onStartOrPauseClick 开始/暂停按钮点击回调
 * @param onStopClick 停止按钮点击回调
 * @param onSkipClick 跳至下一阶段按钮点击回调
 * @param modifier 修饰符
 */
@Composable
fun TimerControlButtons(
    isRunning: Boolean,
    onStartOrPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 暂停/继续按钮
        IconButton(onClick = onStartOrPauseClick) {
            Icon(
                imageVector = if (isRunning) Icons.Default.PauseCircle else Icons.Default.PlayArrow,
                contentDescription = if (isRunning) "暂停" else "继续",
                tint = Color.Unspecified
            )
        }

        // 停止按钮
        IconButton(onClick = onStopClick) {
            Icon(
                imageVector = Icons.Default.StopCircle,
                contentDescription = "停止",
                tint = Color.Unspecified
            )
        }

        // 跳至下一阶段按钮
        IconButton(onClick = onSkipClick) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "跳至下一阶段",
                tint = Color.Unspecified
            )
        }
    }
}
