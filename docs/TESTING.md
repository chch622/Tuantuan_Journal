# TESTING.md — Tuantuan Journal 测试规范

> **本文件定义 Tuantuan Journal 的测试策略和规范。**
> **测试保障数据安全，核心逻辑必须有测试覆盖。**

---

## 1. 测试策略

### 1.1 测试金字塔

```
        /  E2E  \           少量 — 关键用户流程
       /---------\
      / Integration \       适量 — 数据层、Repository
     /---------------\
    /   Unit Tests     \    大量 — UseCase、ViewModel、工具
```

### 1.2 覆盖率目标

| 层 | 目标覆盖率 | 优先级 |
|----|----------|--------|
| Domain (UseCase) | 80%+ | 最高 |
| Data (Repository) | 70%+ | 高 |
| Data (Mapper) | 90%+ | 高 |
| ViewModel | 60%+ | 中 |
| UI (Compose) | 关键路径 | 中 |
| Utility | 90%+ | 高 |

---

## 2. 单元测试

### 2.1 框架

```kotlin
// 依赖
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
testImplementation("com.google.truth:truth:1.1.5")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
```

### 2.2 UseCase 测试

```kotlin
class CreateDiaryEntryUseCaseTest {
    @Mock private lateinit var diaryRepository: DiaryRepository
    private lateinit var useCase: CreateDiaryEntryUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = CreateDiaryEntryUseCase(diaryRepository)
    }

    @Test
    fun `invoke with valid data returns success`() = runTest {
        // Given
        val entry = DiaryEntry(...)
        whenever(diaryRepository.insertEntry(any())).thenReturn(Result.success(entry.id))

        // When
        val result = useCase(entry)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke with empty content returns failure`() = runTest {
        // Given
        val entry = DiaryEntry(content = "")

        // When
        val result = useCase(entry)

        // Then
        assertThat(result.isFailure).isTrue()
    }
}
```

### 2.3 Mapper 测试

```kotlin
class DiaryEntryMapperTest {
    @Test
    fun `entity to domain mapping preserves all fields`() {
        val entity = DiaryEntryEntity(...)
        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.title).isEqualTo(entity.title)
        assertThat(domain.content).isEqualTo(entity.content)
    }

    @Test
    fun `domain to entity mapping preserves all fields`() {
        val domain = DiaryEntry(...)
        val entity = domain.toEntity()

        assertThat(entity.id).isEqualTo(domain.id)
    }
}
```

### 2.4 验证规则测试

- 每个验证规则至少一个正面测试
- 每个验证规则至少一个负面测试
- 边界值测试

---

## 3. 集成测试

### 3.1 框架

```kotlin
androidTestImplementation("androidx.room:room-testing:2.6.1")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test:runner:1.5.2")
```

### 3.2 Room 数据库测试

```kotlin
@RunWith(AndroidJUnit4::class)
class TuantuanDatabaseTest {
    private lateinit var database: TuantuanDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TuantuanDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveDiaryEntry() = runTest {
        val entry = DiaryEntryEntity(...)
        database.diaryEntryDao().insert(entry)

        val retrieved = database.diaryEntryDao().getById(entry.id)
        assertThat(retrieved).isEqualTo(entry)
    }

    @Test
    fun softDeleteFiltersFromQueries() = runTest {
        val entry = DiaryEntryEntity(isDeleted = true, ...)
        database.diaryEntryDao().insert(entry)

        val active = database.diaryEntryDao().getActiveEntries(entry.childId)
        assertThat(active).isEmpty()
    }
}
```

### 3.3 Repository 测试

- 使用真实 Room 数据库（in-memory）
- 测试 Repository 的数据映射
- 测试 Repository 的错误处理
- 测试软删除逻辑

### 3.4 MediaFileManager 测试

- 使用临时目录测试文件操作
- 测试文件复制、删除、缩略图生成
- 测试存储空间计算
- 测试孤立文件清理

---

## 4. UI 测试

### 4.1 框架

```kotlin
androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
androidTestImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")
debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")
```

### 4.2 Compose 测试

```kotlin
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysEmptyState() {
        composeTestRule.setContent {
            HomeScreen(uiState = HomeUiState.Success(emptyList()))
        }

        composeTestRule
            .onNodeWithText("还没有日记")
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysEntries() {
        composeTestRule.setContent {
            HomeScreen(uiState = HomeUiState.Success(testEntries))
        }

        composeTestRule
            .onNodeWithText("团团的日记")
            .assertIsDisplayed()
    }
}
```

### 4.3 关键测试路径

- 首页加载和显示
- 日记创建流程
- 日记编辑流程
- 日记删除确认
- 媒体添加和显示
- 成长记录添加
- 备份和恢复

---

## 5. 测试命名规范

```kotlin
// 格式：`method_state_result`
@Test
fun `insert valid entry returns success`()

@Test
fun `delete entry sets isDeleted true`()

@Test
fun `getActiveEntries filters deleted entries`()

@Test
fun `saveMedia large file returns error`()
```

---

## 6. 测试数据

### 6.1 测试数据工厂

```kotlin
object TestDataFactory {
    fun createChild(
        id: String = UUID.randomUUID().toString(),
        name: String = "测试儿童",
        birthDate: LocalDate = LocalDate.of(2023, 6, 15)
    ) = ChildEntity(id = id, name = name, birthDate = birthDate, ...)

    fun createDiaryEntry(
        id: String = UUID.randomUUID().toString(),
        childId: String = "test-child-id",
        content: String = "测试内容"
    ) = DiaryEntryEntity(id = id, childId = childId, content = content, ...)
}
```

### 6.2 测试数据原则

- 不使用生产数据
- 使用工厂方法创建
- 边界值和极端值
- 包含空值和 null 情况

---

## 7. 持续测试

### 7.1 开发时

- 修改 Domain 层 → 运行相关单元测试
- 修改 Data 层 → 运行相关集成测试
- 修改 UI → 运行相关 UI 测试
- 提交前运行全部测试

### 7.2 CI/CD

- 每次提交运行单元测试
- 每次合并运行全部测试
- 定期运行 E2E 测试

---

## 8. 测试检查清单

每次功能开发时，检查：

- [ ] UseCase 是否有单元测试？
- [ ] Mapper 是否有映射测试？
- [ ] 验证规则是否有正反面测试？
- [ ] Repository 是否有集成测试？
- [ ] 关键 UI 流程是否有测试？
- [ ] 测试是否覆盖错误路径？
- [ ] 测试数据是否使用工厂方法？
- [ ] 测试命名是否规范？

---

*本文件最后更新：Phase 0 — 项目初始化*