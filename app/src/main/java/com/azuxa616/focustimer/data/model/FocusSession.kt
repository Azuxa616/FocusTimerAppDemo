package com.azuxa616.focustimer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 专注会话实体
 *
 * 表示一次完整的专注会话记录，从属于某个事项
 *
 * @param id 唯一ID（主键，自增）
 * @param taskId 所属事项的唯一ID（外键）
 * @param startTimeMillis 开始时间（时间戳，毫秒）
 * @param endTimeMillis 结束时间（时间戳，毫秒，可为null表示未完成）
 * @param focusMinutes 开始时的专注时间设置（分钟）
 * @param breakMinutes 开始时的休息时间设置（分钟）
 * @param cycles 开始时的循环次数设置
 * @param actualTotalMinutes 实际的总专注时间（分钟，不一定等于设定的专注时间*循环次数）
 */
@Entity(
    tableName = "focus_sessions",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["taskId"])]
)
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long,
    val startTimeMillis: Long,
    val endTimeMillis: Long? = null,
    val focusMinutes: Int,
    val breakMinutes: Int,
    val cycles: Int,
    val actualTotalMinutes: Int
)

