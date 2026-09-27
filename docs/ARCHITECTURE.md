# ARCHITECTURE.md — Tuantuan Journal 技术架构

> **本文件定义 Tuantuan Journal 的技术架构和分层规则。**
> **所有代码实现必须遵循本架构，禁止跨层直接访问。**

---

## 1. 架构总览

```
┌─────────────────────────────────────┐
│           Presentation              │
│   Compose UI  +  ViewModel          │
├─────────────────────────────────────┤
│             Domain                  │
│   UseCase  +  Domain Model          │
├─────────────────────────────────────┤
│              Data                   │
│   Repository  +  DataSource         │
│   Room  +  File System  +  DataStore│
└─────────────────────────────────────┘
```

---

## 2. 分层规则

### 2.1 Presentation 层

**组成：** Jetpack Compose UI + ViewModel

**职责：**
- 渲染 UI 状态
- 处理用户交互
- 调用 UseCase 或 ViewModel 方法
- 管理 UI 状态（Loading / Success / Error）

**禁止：**
- 直接访问 Room 数据库
- 直接操作文件系统
- 包含业务逻辑
- 直接引用 Repository

**允许：**
- 调用 ViewModel 方法
- 观察 StateFlow / SharedFlow
- 使用 Compose 状态管理

### 2.2 Domain 层

**组成：** UseCase + Domain Model

**职责：**
- 封装业务规则
- 协调多个 Repository
- 数据转换和验证
- 业务逻辑处理

**禁止：**
- 依赖 Android 框架类
- 依赖 Compose
- 直接操作数据源
- 包含 UI 逻辑

**允许：**
- 调用 Repository 接口
- 定义 Domain Model
- 实现业务验证规则

### 2.3 Data 层

**组成：** Repository 实现 + DataSource

**职责：**
- 实现 Repository 接口
- 管理数据来源（Room / 文件 / DataStore）
- 数据映射（Entity ↔ Domain Model）
- 缓存策略
- 媒体文件管理

**禁止：**
- 依赖 Compose
- 依赖 ViewModel
- 包含业务逻辑
- 直接暴露 Entity 给 Domain 层

**允许：**
- 操作 Room DAO
- 读写文件系统
- 读写 DataStore
- 数据转换

---

## 3. 依赖规则

```
Presentation → Domain ← Data
```

- Presentation 依赖 Domain
- Data 依赖 Domain
- Domain 不依赖任何层
- 依赖方向单向，禁止反向依赖

---

## 4. 包结构

```
com.tuantuan.journal/
├── di/                     # 依赖注入 (Hilt)
│   ├── DatabaseModule.kt
│   ├── RepositoryModule.kt
│   └── UseCaseModule.kt
│
├── data/
│   ├── local/
│   │   ├── db/             # Room 数据库
│   │   │   ├── TuantuanDatabase.kt
│   │   │   ├── dao/
│   │   │   └── entity/
│   │   ├── datastore/      # DataStore 偏好
│   │   └── file/           # 文件管理
│   │       ├── MediaFileManager.kt
│   │       └── BackupManager.kt
│   ├── repository/         # Repository 实现
│   └── mapper/             # Entity ↔ Domain 映射
│
├── domain/
│   ├── model/              # Domain Model
│   ├── repository/         # Repository 接口
│   ├── usecase/            # UseCase
│   └── util/               # Domain 工具
│
├── ui/
│   ├── theme/              # 主题和设计系统
│   │   ├── Theme.kt
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Shape.kt
│   ├── component/          # 共享组件
│   ├── navigation/         # 导航
│   ├── home/               # 首页
│   ├── record/             # 记录
│   ├── growth/             # 成长
│   ├── settings/           # 设置
│   └── backup/             # 备份
│
├── util/                   # 通用工具
│   ├── DateTimeUtil.kt
│   ├── AgeCalculator.kt
│   └── FileUtil.kt
│
└── TuantuanApplication.kt  # Application
```

---

## 5. 关键架构决策

### 5.1 为什么使用 Clean Architecture

- 业务逻辑与 UI 解耦
- 数据源可替换
- 易于测试
- 长期维护友好

### 5.2 为什么媒体文件不进入 SQLite

- SQLite 存储大型二进制性能差
- 数据库体积膨胀影响备份和迁移
- 文件系统更适合流式读写
- 数据库保存索引，文件系统保存内容

### 5.3 为什么完全离线

- 儿童数据隐私安全
- 不依赖外部服务
- 长期可用性不受服务影响
- 家庭使用场景不需要联网

### 5.4 为什么使用 Compose

- 声明式 UI 更适合状态驱动
- Material 3 原生支持
- 动画系统更灵活
- Android 官方推荐方向

### 5.5 为什么使用 Room

- Android 官方 ORM
- 编译时 SQL 验证
- Flow 原生支持
- 迁移系统完善

---

## 6. 数据流

### 6.1 读取数据

```
Room DAO (Flow<Entity>)
    → Repository (map to Domain Model)
        → UseCase (business logic)
            → ViewModel (StateFlow<UiState>)
                → Compose UI (collectAsState)
```

### 6.2 写入数据

```
Compose UI (event)
    → ViewModel (call UseCase)
        → UseCase (validate + transform)
            → Repository (map to Entity)
                → Room DAO (insert/update)
                    → Flow 自动通知 UI 更新
```

### 6.3 媒体数据流

```
用户选择照片
    → ViewModel
        → UseCase
            → MediaFileManager (copy to app-private)
                → Repository (save MediaItem to Room)
                    → Flow 通知 UI 更新
```

---

## 7. 错误处理架构

### 7.1 分层错误处理

| 层 | 错误类型 | 处理方式 |
|----|---------|---------|
| Data | IOException, SQLiteException | 捕获并转为 DomainException |
| Domain | DomainException | 业务逻辑处理或向上传播 |
| Presentation | UiError | 转为用户友好消息 |

### 7.2 UiError 定义

```kotlin
sealed class UiError {
    data class StorageFull(val message: String) : UiError()
    data class MediaNotFound(val message: String) : UiError()
    data class BackupCorrupted(val message: String) : UiError()
    data class DatabaseError(val message: String) : UiError()
    data class GenericError(val message: String) : UiError()
}
```

---

## 8. 状态管理

### 8.1 UiState 模式

```kotlin
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: UiError) : UiState<Nothing>()
}
```

### 8.2 ViewModel 模式

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentEntriesUseCase: GetRecentEntriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            getRecentEntriesUseCase()
                .catch { e -> _uiState.value = HomeUiState.Error(e.toUiError()) }
                .collect { entries -> _uiState.value = HomeUiState.Success(entries) }
        }
    }
}
```

---

## 9. 依赖注入

使用 Hilt 进行依赖注入：

- `@Module` 定义依赖提供
- `@Inject` 构造函数注入
- `@Singleton` 单例（Database、Repository）
- `@ViewModelScoped` ViewModel 作用域

---

## 10. 架构检查清单

每次新增功能时，检查：

- [ ] UI 是否只通过 ViewModel 访问数据？
- [ ] ViewModel 是否只通过 UseCase 访问业务逻辑？
- [ ] UseCase 是否只通过 Repository 接口访问数据？
- [ ] Repository 是否正确映射 Entity 和 Domain Model？
- [ ] 新增类是否在正确的包中？
- [ ] 依赖方向是否正确？
- [ ] 错误处理是否遵循分层规则？

---

*本文件最后更新：Phase 0 — 项目初始化*