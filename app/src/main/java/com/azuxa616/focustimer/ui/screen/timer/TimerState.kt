/**
 * 番茄钟状态模块
 *
 * 定义番茄钟页面的UI状态数据类
 */
package com.azuxa616.focustimer.ui.screen.timer

/**
 * 番茄钟UI状态
 *
 * @param isInFocusPhase 当前是否在专注阶段（false表示休息阶段）
 * @param totalSeconds 当前阶段的总秒数
 * @param remainingSeconds 剩余秒数
 * @param isRunning 计时器是否正在运行
 * @param selectedTaskId 选中的事项ID（可为null）
 * @param currentFocusMinutes 当前设置的专注时间（分钟）
 * @param currentBreakMinutes 当前设置的休息时间（分钟）
 * @param currentCycles 当前设置的循环次数
 * @param currentCycleIndex 当前循环序号（从1开始）
 */
data class TimerState(
    val isInFocusPhase: Boolean,
    val totalSeconds: Long,
    val remainingSeconds: Long,
    val isRunning: Boolean,
    val selectedTaskId: Long?,
    val currentFocusMinutes: Int,
    val currentBreakMinutes: Int,
    val currentCycles: Int,
    val currentCycleIndex: Int
)
