/**
 * 导航图模块
 *
 * 使用Navigation Compose实现页面导航，
 * 包含底部导航栏和三个主要页面的路由配置
 */
package com.azuxa616.focustimer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.azuxa616.focustimer.data.repository.FocusRepository
import com.azuxa616.focustimer.data.repository.SettingsRepository
import com.azuxa616.focustimer.data.repository.TaskRepository
import com.azuxa616.focustimer.ui.screen.settings.SettingsScreen
import com.azuxa616.focustimer.ui.screen.settings.SettingsViewModel
import com.azuxa616.focustimer.ui.screen.statistics.StatisticsScreen
import com.azuxa616.focustimer.ui.screen.statistics.StatisticsViewModel
import com.azuxa616.focustimer.ui.screen.timer.TimerScreen
import com.azuxa616.focustimer.ui.screen.timer.TimerViewModel

// ==================== 路由常量定义 ====================

/** 番茄钟页面路由 */
private const val ROUTE_TIMER = "timer"

/** 统计页面路由 */
private const val ROUTE_STATISTICS = "statistics"

/** 设置页面路由 */
private const val ROUTE_SETTINGS = "settings"

// ==================== 导航图组件 ====================

/**
 * 应用导航图
 *
 * 负责：
 * 1. 管理页面间的导航
 * 2. 渲染底部导航栏
 * 3. 为每个页面创建对应的ViewModel
 *
 * @param focusRepository 专注会话仓库
 * @param taskRepository 事项仓库
 * @param settingsRepository 设置仓库
 */
@Composable
fun AppNavGraph(
    focusRepository: FocusRepository,
    taskRepository: TaskRepository,
    settingsRepository: SettingsRepository
) {
    // 创建导航控制器
    val navController = rememberNavController()

    // 定义底部导航栏的目的地
    val destinations = listOf(
        ROUTE_TIMER to "番茄钟",
        ROUTE_STATISTICS to "统计",
        ROUTE_SETTINGS to "设置"
    )

    // 使用Scaffold布局，包含底部导航栏
    Scaffold(
        bottomBar = {
            // 底部导航栏
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 遍历所有目的地，创建导航项
                destinations.forEach { (route, label) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            // 导航到目标页面
                            navController.navigate(route) {
                                // 弹出到起始目的地，避免回退栈过深
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // 避免重复创建同一页面
                                launchSingleTop = true
                                // 恢复之前保存的状态
                                restoreState = true
                            }
                        },
                        icon = {},
                        label = { Text(text = label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 导航主机，管理页面切换
        NavHost(
            navController = navController,
            startDestination = ROUTE_TIMER,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 番茄钟页面
            composable(ROUTE_TIMER) {
                val viewModel: TimerViewModel = viewModel(
                    factory = TimerViewModel.Companion.Factory(
                        focusRepository = focusRepository,
                        taskRepository = taskRepository
                    )
                )
                TimerScreen(viewModel = viewModel)
            }

            // 统计页面
            composable(ROUTE_STATISTICS) {
                val viewModel: StatisticsViewModel = viewModel(
                    factory = StatisticsViewModel.Companion.Factory(
                        focusRepository = focusRepository,
                        taskRepository = taskRepository
                    )
                )
                StatisticsScreen(viewModel = viewModel)
            }

            // 设置页面
            composable(ROUTE_SETTINGS) {
                val viewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModel.Companion.Factory(
                        taskRepository = taskRepository,
                        settingsRepository = settingsRepository
                    )
                )
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}
