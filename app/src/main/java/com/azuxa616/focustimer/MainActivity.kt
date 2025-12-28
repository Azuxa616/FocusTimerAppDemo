/**
 * 主Activity模块
 *
 * 作为应用的唯一Activity，采用单Activity + 多Compose页面的架构模式
 */
package com.azuxa616.focustimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * 应用主Activity
 *
 * 职责：
 * - 作为Compose UI的宿主
 * - 启用边到边显示模式
 * - 设置Compose内容
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用边到边显示，让内容延伸到系统栏区域
        enableEdgeToEdge()
        // 设置Compose内容
        setContent {
            AppRoot()
        }
    }
}
