/**
 * 主题配置模块
 *
 * 定义应用的Material3主题，支持：
 * - 动态颜色（Android 12+）
 * - 深色/浅色模式自动切换
 */
package com.azuxa616.focustimer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// ==================== 颜色方案定义 ====================

/**
 * 深色模式颜色方案
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

/**
 * 浅色模式颜色方案
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// ==================== 主题组件 ====================

/**
 * FocusTimer应用主题
 *
 * @param darkTheme 是否使用深色模式，默认跟随系统设置
 * @param dynamicColor 是否启用动态颜色（仅Android 12+支持），默认启用
 * @param content 主题包裹的内容
 */
@Composable
fun FocusTimerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // 根据条件选择合适的颜色方案
    val colorScheme = when {
        // Android 12+支持动态颜色，从壁纸提取主题色
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // 深色模式
        darkTheme -> DarkColorScheme
        // 浅色模式
        else -> LightColorScheme
    }

    // 应用Material3主题
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
