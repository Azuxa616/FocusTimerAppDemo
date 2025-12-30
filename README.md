# FocusTimer - 番茄钟专注统计应用

## 项目简介

FocusTimer 是一款基于 Android 平台的番茄钟（Pomodoro）应用，帮助用户通过番茄工作法提升专注效率，同时记录和展示专注数据。应用采用现代化的 Kotlin + Jetpack Compose 技术栈，遵循 MVVM 架构模式，强调状态驱动的 UI 设计思想。

## 功能特性

### 核心功能

- **番茄钟计时器**
  - 标准番茄钟流程（专注/休息循环）
  - 可配置的专注时长（5-60分钟）
  - 可配置的休息时长（1-30分钟）
  - 支持多循环设置（1-10次）
  - 开始、暂停、停止、跳过阶段控制
  - 圆形进度显示，直观展示剩余时间

- **事项管理**
  - 创建、编辑、删除专注事项
  - 每个事项可设置默认的专注时间、休息时间和循环次数
  - 专注时自动记录关联的事项

- **专注统计**
  - 总专注次数和时长统计
  - 平均每次专注时长计算
  - 每日/每周/每月/每年/全部时间范围筛选
  - 任务分布饼图展示
  - 成就系统（连续专注天数、累计时长里程碑）

- **历史记录**
  - 按时间倒序展示专注记录
  - 可展开查看详细信息（开始/结束时间、循环次数）

- **系统设置**
  - 震动提示开关
  - 测试数据导入（开发调试用）

## 技术栈

| 类别 | 技术选型 |
|------|---------|
| 开发语言 | Kotlin |
| UI 框架 | Jetpack Compose |
| 架构模式 | MVVM (Model-View-ViewModel) |
| 导航 | Navigation Compose |
| 数据库 | Room Database |
| 偏好设置 | DataStore Preferences |
| 异步处理 | Kotlin Coroutines + Flow |
| 依赖注入 | 手动依赖注入（Application 级别） |
| 图表展示 | Vico Charts + 自定义 Canvas |
| 最低支持版本 | Android SDK 35 |
| 目标版本 | Android SDK 36 |

## 项目架构

### 架构分层

```
┌─────────────────────────────────────────────────────┐
│                    UI Layer                         │
│    (Screen, Component, Theme, Navigation)           │
├─────────────────────────────────────────────────────┤
│                 ViewModel Layer                     │
│         (状态管理, 用户交互处理)                      │
├─────────────────────────────────────────────────────┤
│              Business Logic Layer                   │
│    (Calculator, Formatter - 计算与格式化工具)        │
├─────────────────────────────────────────────────────┤
│                Repository Layer                     │
│           (数据访问抽象, 数据转换)                    │
├─────────────────────────────────────────────────────┤
│                  Data Layer                         │
│       (Room DAO, DataStore, Entity)                 │
└─────────────────────────────────────────────────────┘
```

### 模块结构

```
com.azuxa616.focustimer
├── App.kt                          # Application 入口，依赖初始化
├── MainActivity.kt                 # 主 Activity
├── data/                          # 数据层
│   ├── local/                     # 本地数据源
│   │   ├── AppDatabase.kt         # Room 数据库定义
│   │   ├── FocusSessionDao.kt     # 专注会话 DAO
│   │   └── TaskDao.kt             # 事项 DAO
│   ├── model/                     # 数据模型
│   │   ├── FocusSession.kt        # 专注会话实体
│   │   └── Task.kt                # 事项实体
│   ├── repository/                # 数据仓库
│   │   ├── FocusRepository.kt     # 专注会话仓库
│   │   ├── TaskRepository.kt      # 事项仓库
│   │   └── SettingsRepository.kt  # 设置仓库
│   └── SqlScriptExecutor.kt       # SQL 脚本执行器
├── ui/                            # UI 层
│   ├── component/                 # 可复用组件
│   │   ├── AchievementCard.kt     # 成就卡片
│   │   ├── DailySummaryCard.kt    # 每日汇总卡片
│   │   ├── FocusComparisonChart.kt# 对比柱状图
│   │   ├── FocusSessionItem.kt    # 会话记录项
│   │   ├── FocusTrendChart.kt     # 趋势折线图
│   │   ├── ImportTestDataSection.kt# 导入测试数据组件
│   │   ├── SettingItem.kt         # 设置项组件
│   │   ├── StatisticsOverviewCard.kt# 统计概览卡片
│   │   ├── TabSelector.kt         # Tab 选择器
│   │   ├── TaskDistributionChart.kt# 任务分布饼图
│   │   ├── TaskEditDialog.kt      # 任务编辑对话框
│   │   ├── TaskStatisticsCard.kt  # 任务统计卡片
│   │   ├── TimerCircle.kt         # 计时器圆环
│   │   └── TimerControlButtons.kt # 控制按钮组
│   ├── navigation/                # 导航
│   │   └── AppNavGraph.kt         # 应用导航图
│   ├── screen/                    # 页面
│   │   ├── timer/                 # 番茄钟页面
│   │   │   ├── TimerScreen.kt
│   │   │   ├── TimerState.kt
│   │   │   └── TimerViewModel.kt
│   │   ├── statistics/            # 统计页面
│   │   │   ├── StatisticsScreen.kt
│   │   │   ├── StatisticsState.kt
│   │   │   └── StatisticsViewModel.kt
│   │   └── settings/              # 设置页面
│   │       ├── SettingsScreen.kt
│   │       ├── SettingsState.kt
│   │       └── SettingsViewModel.kt
│   └── theme/                     # 主题
│       ├── Color.kt               # 颜色定义
│       ├── Theme.kt               # 主题配置
│       └── Type.kt                # 字体排版
└── util/                          # 工具类
    ├── DateUtils.kt               # 日期工具
    ├── TimeFormatter.kt           # 时间格式化
    ├── SessionFormatter.kt        # 会话格式化
    ├── StatisticsCalculator.kt    # 统计计算器
    └── TimeRangeCalculator.kt     # 时间范围计算器
```

## 数据模型

### Task（事项）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| name | String | 事项名称 |
| defaultFocusMinutes | Int | 默认专注时间（分钟） |
| defaultBreakMinutes | Int | 默认休息时间（分钟） |
| defaultCycles | Int | 默认循环次数 |

### FocusSession（专注会话）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| taskId | Long | 关联事项 ID（外键） |
| startTimeMillis | Long | 开始时间戳 |
| endTimeMillis | Long? | 结束时间戳（可空） |
| focusMinutes | Int | 专注时间设置 |
| breakMinutes | Int | 休息时间设置 |
| cycles | Int | 循环次数设置 |
| actualTotalMinutes | Int | 实际专注总时长 |

## 构建与运行

### 环境要求

- Android Studio Ladybug (2024.2.1) 或更高版本
- JDK 11 或更高版本
- Android SDK 35+
- Gradle 8.x

### 构建步骤

1. 克隆项目到本地
2. 使用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器（API 35+）
5. 点击运行按钮或执行 `./gradlew installDebug`

### 运行测试

```bash
# 运行单元测试
./gradlew test

# 运行 Instrumented 测试
./gradlew connectedAndroidTest
```

## 开发规范

### 代码风格

- **语言**：Kotlin，遵循 Kotlin 官方编码规范
- **注释**：使用中文注释
- **命名规范**：
  - 类名：PascalCase（如 `TimerViewModel`）
  - 函数/变量：camelCase（如 `calculateDailySummaries`）
  - 常量：SCREAMING_SNAKE_CASE（如 `DEFAULT_FOCUS_MINUTES`）

### 架构规范

- **状态管理**：使用 StateFlow 管理 UI 状态
- **异步操作**：使用 Kotlin Coroutines 和 Flow
- **依赖注入**：在 Application 中初始化，通过参数传递
- **组件化**：UI 组件尽量无状态，状态由 ViewModel 管理

### 文件组织

- 每个 Screen 对应一个文件夹，包含 Screen、State、ViewModel 三个文件
- 可复用的 UI 组件放在 `component` 目录
- 业务逻辑工具类放在 `util` 目录
- 数据相关代码放在 `data` 目录

## 版本信息

- **当前版本**：1.0.0
- **版本代码**：1
- **应用包名**：com.azuxa616.focustimer

## 许可证

本项目仅供学习和课程作业使用。

---

*本文档最后更新于 2025年1月*

