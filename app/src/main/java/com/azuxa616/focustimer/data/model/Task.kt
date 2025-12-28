package com.azuxa616.focustimer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 事项实体
 *
 * 表示一个专注事项，包含默认的专注设置
 *
 * @param id 唯一ID（主键，自增）
 * @param name 事项名称
 * @param defaultFocusMinutes 默认专注时间（分钟）
 * @param defaultBreakMinutes 默认休息时间（分钟）
 * @param defaultCycles 默认循环次数
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val defaultFocusMinutes: Int,
    val defaultBreakMinutes: Int,
    val defaultCycles: Int
)

