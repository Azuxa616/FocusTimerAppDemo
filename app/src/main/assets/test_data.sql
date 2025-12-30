-- FocusTimer 测试数据脚本
-- 用于向 Room 数据库插入测试数据
-- 数据库文件: focus_timer.db

-- ==================== 清理现有测试数据（可选） ====================
DELETE FROM focus_sessions;
DELETE FROM tasks;

-- ==================== 插入任务数据 ====================

-- 任务1: 学习编程
INSERT INTO tasks (id, name, defaultFocusMinutes, defaultBreakMinutes, defaultCycles) 
VALUES (1, '学习编程', 25, 5, 1);

-- 任务2: 阅读书籍
INSERT INTO tasks (id, name, defaultFocusMinutes, defaultBreakMinutes, defaultCycles) 
VALUES (2, '阅读书籍', 45, 10, 1);

-- 任务3: 工作项目
INSERT INTO tasks (id, name, defaultFocusMinutes, defaultBreakMinutes, defaultCycles) 
VALUES (3, '工作项目', 25, 5, 2);

-- 任务4: 运动健身
INSERT INTO tasks (id, name, defaultFocusMinutes, defaultBreakMinutes, defaultCycles) 
VALUES (4, '运动健身', 30, 10, 1);

-- 任务5: 写作练习
INSERT INTO tasks (id, name, defaultFocusMinutes, defaultBreakMinutes, defaultCycles) 
VALUES (5, '写作练习', 25, 5, 1);

-- ==================== 插入专注会话数据 ====================
-- 时间戳说明：
-- 基准时间：2024-12-20 00:00:00 (毫秒时间戳: 1734652800000)
-- 以下时间戳基于此基准计算，覆盖最近7天的数据

-- ==================== 任务1: 学习编程 - 会话记录 ====================

-- 7天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734052800000, 1734054300000, 25, 5, 1, 25);

-- 6天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734139200000, 1734140700000, 25, 5, 1, 25);

-- 5天前 - 已完成（多循环）
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734225600000, 1734229500000, 25, 5, 2, 50);

-- 4天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734312000000, 1734313500000, 25, 5, 1, 25);

-- 3天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734398400000, 1734399900000, 25, 5, 1, 25);

-- 2天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734484800000, 1734486300000, 25, 5, 1, 25);

-- 1天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734571200000, 1734572700000, 25, 5, 1, 25);

-- 今天 - 未完成（endTimeMillis 为 NULL）
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (1, 1734652800000, NULL, 25, 5, 1, 0);

-- ==================== 任务2: 阅读书籍 - 会话记录 ====================

-- 7天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734056400000, 1734059100000, 45, 10, 1, 45);

-- 6天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734142800000, 1734145500000, 45, 10, 1, 45);

-- 5天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734229200000, 1734231900000, 45, 10, 1, 45);

-- 4天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734315600000, 1734318300000, 45, 10, 1, 45);

-- 3天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734402000000, 1734404700000, 45, 10, 1, 45);

-- 2天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734488400000, 1734491100000, 45, 10, 1, 45);

-- 1天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (2, 1734574800000, 1734577500000, 45, 10, 1, 45);

-- ==================== 任务3: 工作项目 - 会话记录 ====================

-- 7天前 - 已完成（多循环）
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734060000000, 1734063900000, 25, 5, 2, 50);

-- 6天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734146400000, 1734147900000, 25, 5, 1, 25);

-- 5天前 - 已完成（多循环）
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734232800000, 1734236700000, 25, 5, 2, 50);

-- 4天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734319200000, 1734320700000, 25, 5, 1, 25);

-- 3天前 - 已完成（多循环）
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734405600000, 1734409500000, 25, 5, 2, 50);

-- 2天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734492000000, 1734493500000, 25, 5, 1, 25);

-- 1天前 - 已完成（多循环）
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734578400000, 1734582300000, 25, 5, 2, 50);

-- 今天 - 未完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (3, 1734656400000, NULL, 25, 5, 1, 0);

-- ==================== 任务4: 运动健身 - 会话记录 ====================

-- 6天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (4, 1734148800000, 1734150600000, 30, 10, 1, 30);

-- 5天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (4, 1734235200000, 1734237000000, 30, 10, 1, 30);

-- 4天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (4, 1734321600000, 1734323400000, 30, 10, 1, 30);

-- 3天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (4, 1734408000000, 1734409800000, 30, 10, 1, 30);

-- 2天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (4, 1734494400000, 1734496200000, 30, 10, 1, 30);

-- ==================== 任务5: 写作练习 - 会话记录 ====================

-- 7天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734063600000, 1734065100000, 25, 5, 1, 25);

-- 6天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734150000000, 1734151500000, 25, 5, 1, 25);

-- 5天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734236400000, 1734237900000, 25, 5, 1, 25);

-- 4天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734322800000, 1734324300000, 25, 5, 1, 25);

-- 3天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734409200000, 1734410700000, 25, 5, 1, 25);

-- 2天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734495600000, 1734497100000, 25, 5, 1, 25);

-- 1天前 - 已完成
INSERT INTO focus_sessions (taskId, startTimeMillis, endTimeMillis, focusMinutes, breakMinutes, cycles, actualTotalMinutes)
VALUES (5, 1734582000000, 1734583500000, 25, 5, 1, 25);

