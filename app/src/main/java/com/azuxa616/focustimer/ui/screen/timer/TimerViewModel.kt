/**
 * 番茄钟ViewModel模块
 *
 * 管理番茄钟的核心计时逻辑：
 * - 事项选择和管理
 * - 专注/休息模式切换
 * - 循环计数
 * - 倒计时控制
 * - 专注会话记录保存
 */
package com.azuxa616.focustimer.ui.screen.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azuxa616.focustimer.data.model.FocusSession
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.data.repository.FocusRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ==================== 番茄钟ViewModel ====================

/**
 * 番茄钟ViewModel
 *
 * 职责：
 * 1. 管理计时器状态
 * 2. 处理事项选择
 * 3. 处理开始、暂停、停止、跳至下一阶段操作
 * 4. 管理循环逻辑
 * 5. 记录完成的专注会话
 *
 * @param focusRepository 专注会话仓库
 * @param taskRepository 事项仓库
 */
class TimerViewModel(
    private val focusRepository: FocusRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    // ==================== 状态管理 ====================

    /** 内部可变状态 */
    private val mutableState = MutableStateFlow(
        TimerState(
            isInSession = false,
            isInFocusPhase = true,
            totalSeconds = DEFAULT_FOCUS_MINUTES.toLong() * SECONDS_PER_MINUTE,
            remainingSeconds = DEFAULT_FOCUS_MINUTES.toLong() * SECONDS_PER_MINUTE,
            isRunning = false,
            selectedTaskId = null,
            currentFocusMinutes = DEFAULT_FOCUS_MINUTES,
            currentBreakMinutes = DEFAULT_BREAK_MINUTES,
            currentCycles = 1,
            currentCycleIndex = 1
        )
    )

    /** 对外暴露的只读状态 */
    val state: StateFlow<TimerState> = mutableState

    /** 所有事项的Flow */
    val allTasks = taskRepository.allTasks

    /** 计时任务 */
    private var tickJob: Job? = null

    /** 当前专注会话的开始时间 */
    private var currentSessionStartTimeMillis: Long? = null

    /** 累计的实际专注时间（分钟） */
    private var accumulatedFocusMinutes: Int = 0

    /** 当前专注阶段的开始时间（用于计算实际专注时间） */
    private var currentFocusPhaseStartTimeMillis: Long? = null

    // ==================== 初始化 ====================

    init {
        // 监听事项列表，如果有事项且未选择，选择第一个
        viewModelScope.launch {
            taskRepository.allTasks.collect { tasks ->
                val currentState = mutableState.value
                if (tasks.isNotEmpty() && currentState.selectedTaskId == null) {
                    // 选择第一个事项
                    selectTask(tasks.first().id)
                }
            }
        }
    }

    // ==================== 公开方法 ====================

    /**
     * 选择事项
     */
    fun selectTask(taskId: Long) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId).first()
            task?.let {
                mutableState.value = mutableState.value.copy(
                    selectedTaskId = taskId,
                    currentFocusMinutes = it.defaultFocusMinutes,
                    currentBreakMinutes = it.defaultBreakMinutes,
                    currentCycles = it.defaultCycles,
                    currentCycleIndex = 1,
                    isInSession = false,
                    isInFocusPhase = true,
                    totalSeconds = it.defaultFocusMinutes.toLong() * SECONDS_PER_MINUTE,
                    remainingSeconds = it.defaultFocusMinutes.toLong() * SECONDS_PER_MINUTE,
                    isRunning = false
                )
                // 重置计时器
                resetTimer()
            }
        }
    }

    /**
     * 更新专注时间设置
     */
    fun updateFocusMinutes(minutes: Int) {
        val current = mutableState.value
        if (!current.isRunning && current.isInFocusPhase) {
            mutableState.value = current.copy(
                currentFocusMinutes = minutes,
                totalSeconds = minutes.toLong() * SECONDS_PER_MINUTE,
                remainingSeconds = minutes.toLong() * SECONDS_PER_MINUTE
            )
        } else {
            mutableState.value = current.copy(currentFocusMinutes = minutes)
        }
    }

    /**
     * 更新休息时间设置
     */
    fun updateBreakMinutes(minutes: Int) {
        val current = mutableState.value
        if (!current.isRunning && !current.isInFocusPhase) {
            mutableState.value = current.copy(
                currentBreakMinutes = minutes,
                totalSeconds = minutes.toLong() * SECONDS_PER_MINUTE,
                remainingSeconds = minutes.toLong() * SECONDS_PER_MINUTE
            )
        } else {
            mutableState.value = current.copy(currentBreakMinutes = minutes)
        }
    }

    /**
     * 更新循环次数设置
     */
    fun updateCycles(cycles: Int) {
        mutableState.value = mutableState.value.copy(currentCycles = cycles)
    }

    /**
     * 重置为当前事项的默认设置
     */
    fun resetToDefaultSettings() {
        val current = mutableState.value
        current.selectedTaskId?.let { taskId ->
            viewModelScope.launch {
                val task = taskRepository.getTaskById(taskId).first()
                task?.let {
                    mutableState.value = mutableState.value.copy(
                        currentFocusMinutes = it.defaultFocusMinutes,
                        currentBreakMinutes = it.defaultBreakMinutes,
                        currentCycles = it.defaultCycles,
                        currentCycleIndex = 1,
                        isInSession = false,
                        isInFocusPhase = true,
                        totalSeconds = it.defaultFocusMinutes.toLong() * SECONDS_PER_MINUTE,
                        remainingSeconds = it.defaultFocusMinutes.toLong() * SECONDS_PER_MINUTE,
                        isRunning = false
                    )
                    resetTimer()
                }
            }
        }
    }

    /**
     * 开始或继续计时
     */
    fun startOrResume() {
        val current = mutableState.value

        // 如果已经在运行，则暂停
        if (current.isRunning) {
            pause()
            return
        }

        // 如果不在任务中，开始新任务
        if (!current.isInSession) {
            // 重置为专注阶段
            mutableState.value = current.copy(
                isInSession = true,
                isInFocusPhase = true,
                currentCycleIndex = 1,
                totalSeconds = current.currentFocusMinutes.toLong() * SECONDS_PER_MINUTE,
                remainingSeconds = current.currentFocusMinutes.toLong() * SECONDS_PER_MINUTE,
                isRunning = false
            )
        }

        // 如果时间已用完，先重置
        val updatedState = mutableState.value
        if (updatedState.remainingSeconds <= 0) {
            resetTimer()
        }

        // 记录会话开始时间（如果是第一次开始）
        if (currentSessionStartTimeMillis == null) {
            currentSessionStartTimeMillis = System.currentTimeMillis()
        }

        // 记录专注阶段开始时间（如果进入专注阶段）
        val finalState = mutableState.value
        if (finalState.isInFocusPhase && currentFocusPhaseStartTimeMillis == null) {
            currentFocusPhaseStartTimeMillis = System.currentTimeMillis()
        }

        // 更新状态为运行中
        mutableState.value = finalState.copy(isRunning = true)

        // 开始计时
        startTicking()
    }

    /**
     * 暂停计时
     */
    fun pause() {
        val current = mutableState.value

        // 如果未运行，忽略
        if (!current.isRunning) {
            return
        }

        // 更新累计专注时间
        updateAccumulatedFocusTime()

        // 更新状态为暂停
        mutableState.value = current.copy(isRunning = false)

        // 取消计时任务
        tickJob?.cancel()
        tickJob = null
    }

    /**
     * 停止计时并保存会话
     */
    fun stop() {
        val current = mutableState.value
        val startTime = currentSessionStartTimeMillis

        // 取消计时任务
        tickJob?.cancel()
        tickJob = null

        // 更新累计专注时间
        updateAccumulatedFocusTime()

        // 如果有开始时间，保存会话记录
        if (startTime != null && current.selectedTaskId != null) {
            viewModelScope.launch {
                val session = FocusSession(
                    taskId = current.selectedTaskId!!,
                    startTimeMillis = startTime,
                    endTimeMillis = System.currentTimeMillis(),
                    focusMinutes = current.currentFocusMinutes,
                    breakMinutes = current.currentBreakMinutes,
                    cycles = current.currentCycles,
                    actualTotalMinutes = accumulatedFocusMinutes
                )
                focusRepository.addFocusSession(session)
            }
        }

        // 重置状态为初始状态（非任务中，专注阶段）
        val resetState = mutableState.value
        mutableState.value = resetState.copy(
            isInSession = false,
            isInFocusPhase = true,
            currentCycleIndex = 1,
            totalSeconds = resetState.currentFocusMinutes.toLong() * SECONDS_PER_MINUTE,
            remainingSeconds = resetState.currentFocusMinutes.toLong() * SECONDS_PER_MINUTE,
            isRunning = false
        )
        tickJob?.cancel()
        tickJob = null
        currentSessionStartTimeMillis = null
        accumulatedFocusMinutes = 0
        currentFocusPhaseStartTimeMillis = null
    }

    /**
     * 跳至下一阶段
     */
    fun skipToNextPhase() {
        val current = mutableState.value

        // 如果未开始会话，忽略
        if (currentSessionStartTimeMillis == null) {
            return
        }

        // 更新累计专注时间
        updateAccumulatedFocusTime()

        // 取消当前计时
        tickJob?.cancel()
        tickJob = null

        if (current.isInFocusPhase) {
            // 当前在专注阶段，切换到休息阶段
            switchToBreakPhase()
        } else {
            // 当前在休息阶段
            if (current.currentCycleIndex < current.currentCycles) {
                // 还有更多循环，进入下一循环的专注阶段
                switchToNextFocusCycle()
            } else {
                // 最后一次循环，结束会话
                stop()
            }
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 重置计时器（不保存会话）
     * 如果不在任务中，重置为专注阶段；如果在任务中，保持当前阶段
     */
    private fun resetTimer() {
        tickJob?.cancel()
        tickJob = null

        val current = mutableState.value
        val totalSeconds = if (!current.isInSession || current.isInFocusPhase) {
            // 非任务中或专注阶段，重置为专注时间
            current.currentFocusMinutes.toLong() * SECONDS_PER_MINUTE
        } else {
            // 任务中的休息阶段，重置为休息时间
            current.currentBreakMinutes.toLong() * SECONDS_PER_MINUTE
        }

        mutableState.value = current.copy(
            totalSeconds = totalSeconds,
            remainingSeconds = totalSeconds,
            isRunning = false
        )
    }

    /**
     * 切换到休息阶段
     */
    private fun switchToBreakPhase() {
        val current = mutableState.value
        val totalSeconds = current.currentBreakMinutes.toLong() * SECONDS_PER_MINUTE

        mutableState.value = current.copy(
            isInFocusPhase = false,
            totalSeconds = totalSeconds,
            remainingSeconds = totalSeconds,
            isRunning = false
        )

        currentFocusPhaseStartTimeMillis = null

        // 如果之前是运行状态，自动开始休息计时
        if (current.isRunning) {
            mutableState.value = mutableState.value.copy(isRunning = true)
            startTicking()
        }
    }

    /**
     * 切换到下一循环的专注阶段
     */
    private fun switchToNextFocusCycle() {
        val current = mutableState.value
        val totalSeconds = current.currentFocusMinutes.toLong() * SECONDS_PER_MINUTE

        mutableState.value = current.copy(
            isInFocusPhase = true,
            currentCycleIndex = current.currentCycleIndex + 1,
            totalSeconds = totalSeconds,
            remainingSeconds = totalSeconds,
            isRunning = false
        )

        currentFocusPhaseStartTimeMillis = System.currentTimeMillis()

        // 如果之前是运行状态，自动开始专注计时
        if (current.isRunning) {
            mutableState.value = mutableState.value.copy(isRunning = true)
            startTicking()
        }
    }

    /**
     * 更新累计专注时间
     */
    private fun updateAccumulatedFocusTime() {
        val startTime = currentFocusPhaseStartTimeMillis
        if (startTime != null && mutableState.value.isInFocusPhase) {
            val elapsedMillis = System.currentTimeMillis() - startTime
            val elapsedMinutes = (elapsedMillis / (1000 * 60)).toInt()
            accumulatedFocusMinutes += elapsedMinutes
            currentFocusPhaseStartTimeMillis = null
        }
    }

    /**
     * 开始每秒计时
     */
    private fun startTicking() {
        // 避免重复启动
        if (tickJob != null) {
            return
        }

        tickJob = viewModelScope.launch {
            while (true) {
                // 每秒更新一次
                delay(TICK_INTERVAL_MILLIS)

                val current = mutableState.value

                // 如果已暂停，继续等待
                if (!current.isRunning) {
                    continue
                }

                // 检查是否完成
                if (current.remainingSeconds <= 1) {
                    handleTimerFinished()
                    break
                } else {
                    // 减少剩余时间
                    mutableState.value = current.copy(
                        remainingSeconds = current.remainingSeconds - 1
                    )
                }
            }
            tickJob = null
        }
    }

    /**
     * 处理计时完成
     */
    private fun handleTimerFinished() {
        viewModelScope.launch {
            val current = mutableState.value

            // 更新累计专注时间
            updateAccumulatedFocusTime()

            // 更新状态为完成
            mutableState.value = current.copy(
                remainingSeconds = 0,
                isRunning = false
            )

            // 根据当前阶段切换
            if (current.isInFocusPhase) {
                // 专注完成，切换到休息阶段
                switchToBreakPhase()
                // 如果设置了自动开始，可以在这里启动休息计时
            } else {
                // 休息完成
                if (current.currentCycleIndex < current.currentCycles) {
                    // 还有更多循环，进入下一循环的专注阶段
                    switchToNextFocusCycle()
                } else {
                    // 最后一次循环完成，结束会话
                    stop()
                }
            }
        }
    }

    // ==================== 伴生对象 ====================

    companion object {
        /** 默认专注时长（分钟） */
        private const val DEFAULT_FOCUS_MINUTES = 25

        /** 默认休息时长（分钟） */
        private const val DEFAULT_BREAK_MINUTES = 5

        /** 每分钟秒数 */
        private const val SECONDS_PER_MINUTE = 60L

        /** 计时间隔（毫秒） */
        private const val TICK_INTERVAL_MILLIS = 1000L

        /**
         * ViewModel工厂类
         */
        class Factory(
            private val focusRepository: FocusRepository,
            private val taskRepository: TaskRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return TimerViewModel(
                        focusRepository = focusRepository,
                        taskRepository = taskRepository
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
