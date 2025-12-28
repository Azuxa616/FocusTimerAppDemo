/**
 * 应用程序入口模块
 *
 * 本文件包含:
 * - FocusTimerApp: 自定义Application类，用于初始化全局依赖
 * - AppRoot: 应用根组件，配置主题和导航
 */
package com.azuxa616.focustimer

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.azuxa616.focustimer.data.local.AppDatabase
import com.azuxa616.focustimer.data.model.Task
import com.azuxa616.focustimer.data.repository.FocusRepository
import com.azuxa616.focustimer.data.repository.SettingsRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import com.azuxa616.focustimer.ui.navigation.AppNavGraph
import com.azuxa616.focustimer.ui.theme.FocusTimerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * 自定义Application类
 *
 * 负责初始化应用级别的依赖项：
 * - 数据库实例
 * - 专注会话仓库
 * - 事项仓库
 * - 设置仓库
 */
class FocusTimerApp : Application() {

    /**
     * 数据库实例
     */
    private val database by lazy {
        AppDatabase.getInstance(this)
    }

    /**
     * 专注会话仓库
     * 使用懒加载方式初始化，确保数据库只创建一次
     */
    val focusRepository by lazy {
        FocusRepository(database.focusSessionDao())
    }

    /**
     * 事项仓库
     * 管理事项的持久化存储
     */
    val taskRepository by lazy {
        TaskRepository(database.taskDao())
    }

    /**
     * 设置仓库
     * 管理应用配置参数的持久化存储
     */
    val settingsRepository by lazy {
        SettingsRepository.fromContext(this)
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化默认事项
        initializeDefaultTask()
    }

    /**
     * 初始化默认事项
     */
    private fun initializeDefaultTask() {
        // 在后台线程中检查并创建默认事项
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        applicationScope.launch {
            val tasks = taskRepository.allTasks.first()
            if (tasks.isEmpty()) {
                val defaultTask = Task(
                    name = "默认事项",
                    defaultFocusMinutes = 25,
                    defaultBreakMinutes = 5,
                    defaultCycles = 1
                )
                taskRepository.insertTask(defaultTask)
            }
        }
    }
}

// ==================== Compose 入口组件 ====================

/**
 * 应用根组件
 *
 * 职责：
 * 1. 获取Application实例中的仓库依赖
 * 2. 应用全局主题
 * 3. 设置导航图
 */
@Composable
fun AppRoot() {
    // 获取Application上下文
    val context = LocalContext.current
    val application = context.applicationContext as FocusTimerApp

    // 获取仓库依赖
    val focusRepository = remember { application.focusRepository }
    val taskRepository = remember { application.taskRepository }
    val settingsRepository = remember { application.settingsRepository }

    // 应用主题并设置导航
    FocusTimerTheme {
        AppNavGraph(
            focusRepository = focusRepository,
            taskRepository = taskRepository,
            settingsRepository = settingsRepository
        )
    }
}
