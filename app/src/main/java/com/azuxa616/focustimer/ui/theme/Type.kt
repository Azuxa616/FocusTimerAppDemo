/**
 * 字体排版模块
 *
 * 定义应用中使用的文字样式，
 * 基于Material3的Typography系统
 */
package com.azuxa616.focustimer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==================== 字体排版定义 ====================

/**
 * 应用字体排版配置
 *
 * 基于Material3的Typography系统，
 * 可根据需要扩展更多文字样式
 */
val Typography = Typography(
    // 正文大号 - 用于主要文本内容
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    // 可扩展其他文字样式：
    // titleLarge - 大标题
    // labelSmall - 小标签
)
