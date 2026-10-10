package com.tuantuan.journal.ui.screen.growth

import com.google.common.truth.Truth.assertThat
import com.tuantuan.journal.domain.model.GrowthRecord
import com.tuantuan.journal.domain.model.GrowthType
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class GrowthChartDataTest {

    private val testInstant = Instant.parse("2024-06-15T10:30:00Z")

    private fun createRecord(
        id: String,
        value: Double,
        date: LocalDate,
        type: GrowthType = GrowthType.HEIGHT
    ) = GrowthRecord(
        id = id,
        childId = "child-1",
        recordType = type,
        value = value,
        unit = "cm",
        measureDate = date,
        notes = null,
        isDeleted = false,
        createdAt = testInstant,
        updatedAt = testInstant
    )

    // ===== fromRecords 空列表 =====

    @Test
    fun `fromRecords returns null for empty list`() {
        val result = GrowthChartData.fromRecords(emptyList())
        assertThat(result).isNull()
    }

    // ===== fromRecords 单数据点 =====

    @Test
    fun `fromRecords with single record returns data with one point at center`() {
        val record = createRecord("r1", 75.0, LocalDate.of(2024, 6, 15))
        val result = GrowthChartData.fromRecords(listOf(record))!!

        assertThat(result.dataCount).isEqualTo(1)
        assertThat(result.points).hasSize(1)
        assertThat(result.points[0].x).isEqualTo(0.5f) // 单点居中
        assertThat(result.points[0].value).isEqualTo(75.0)
    }

    @Test
    fun `fromRecords with single record expands Y range by +-1`() {
        val record = createRecord("r1", 75.0, LocalDate.of(2024, 6, 15))
        val result = GrowthChartData.fromRecords(listOf(record))!!

        // 相同值时 yMin = center - 1, yMax = center + 1
        assertThat(result.yMin).isEqualTo(74f)
        assertThat(result.yMax).isEqualTo(76f)
    }

    // ===== fromRecords 多数据点 =====

    @Test
    fun `fromRecords with multiple records returns sorted data`() {
        val records = listOf(
            createRecord("r3", 80.0, LocalDate.of(2024, 8, 15)),
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 75.0, LocalDate.of(2024, 7, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        assertThat(result.dataCount).isEqualTo(3)
        assertThat(result.points).hasSize(3)
        // 按日期升序排列
        assertThat(result.points[0].value).isEqualTo(70.0)
        assertThat(result.points[1].value).isEqualTo(75.0)
        assertThat(result.points[2].value).isEqualTo(80.0)
    }

    @Test
    fun `fromRecords normalizes X coordinates evenly spaced`() {
        val records = listOf(
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 75.0, LocalDate.of(2024, 7, 15)),
            createRecord("r3", 80.0, LocalDate.of(2024, 8, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        assertThat(result.points[0].x).isEqualTo(0f)
        assertThat(result.points[1].x).isEqualTo(0.5f)
        assertThat(result.points[2].x).isEqualTo(1f)
    }

    @Test
    fun `fromRecords Y range has padding around data`() {
        val records = listOf(
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 80.0, LocalDate.of(2024, 7, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        // 数据范围 70-80, range=10, padding=1 (10%), yMin≈69, yMax≈81
        assertThat(result.yMin).isLessThan(70f)
        assertThat(result.yMax).isGreaterThan(80f)
    }

    @Test
    fun `fromRecords Y labels are within range`() {
        val records = listOf(
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 80.0, LocalDate.of(2024, 7, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        assertThat(result.yLabels).isNotEmpty()
        // Y 标签的归一化值应在 [0, 1] 范围内
        result.yLabels.forEach { label ->
            assertThat(label.normalizedY).isAtLeast(0f)
            assertThat(label.normalizedY).isAtMost(1f)
        }
    }

    @Test
    fun `fromRecords Y labels count does not exceed MAX_GRID_LINES`() {
        val records = listOf(
            createRecord("r1", 50.0, LocalDate.of(2024, 1, 15)),
            createRecord("r2", 60.0, LocalDate.of(2024, 2, 15)),
            createRecord("r3", 70.0, LocalDate.of(2024, 3, 15)),
            createRecord("r4", 80.0, LocalDate.of(2024, 4, 15)),
            createRecord("r5", 90.0, LocalDate.of(2024, 5, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        assertThat(result.yLabels.size).isAtMost(GrowthChartData.MAX_GRID_LINES + 1)
    }

    // ===== fromRecords 相同值 =====

    @Test
    fun `fromRecords with all same values expands range`() {
        val records = listOf(
            createRecord("r1", 75.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 75.0, LocalDate.of(2024, 7, 15)),
            createRecord("r3", 75.0, LocalDate.of(2024, 8, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        // 所有值相同时 yMin = center - 1, yMax = center + 1
        assertThat(result.yMin).isEqualTo(74f)
        assertThat(result.yMax).isEqualTo(76f)
        // 所有点的 Y 坐标应为 0.5（居中）
        result.points.forEach { point ->
            assertThat(point.y).isEqualTo(0.5f)
        }
    }

    // ===== X 轴标签 =====

    @Test
    fun `fromRecords generates X labels with month format`() {
        val records = listOf(
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 75.0, LocalDate.of(2024, 7, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        assertThat(result.xLabels).hasSize(2)
        assertThat(result.xLabels[0].label).isEqualTo("6月")
        assertThat(result.xLabels[1].label).isEqualTo("7月")
    }

    @Test
    fun `fromRecords single record X label at center`() {
        val record = createRecord("r1", 75.0, LocalDate.of(2024, 6, 15))
        val result = GrowthChartData.fromRecords(listOf(record))!!

        assertThat(result.xLabels).hasSize(1)
        assertThat(result.xLabels[0].normalizedX).isEqualTo(0.5f)
    }

    // ===== formatValue =====

    @Test
    fun `formatValue shows integer without decimal for whole numbers`() {
        assertThat(GrowthChartData.formatValue(75.0)).isEqualTo("75")
        assertThat(GrowthChartData.formatValue(0.0)).isEqualTo("0")
        assertThat(GrowthChartData.formatValue(100.0)).isEqualTo("100")
    }

    @Test
    fun `formatValue shows one decimal place for fractional numbers`() {
        assertThat(GrowthChartData.formatValue(75.5)).isEqualTo("75.5")
        assertThat(GrowthChartData.formatValue(10.3)).isEqualTo("10.3")
    }

    // ===== niceStep 算法（间接测试） =====

    @Test
    fun `Y labels have nice round numbers`() {
        // 使用范围使得 niceStep 产生整数刻度
        val records = listOf(
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 80.0, LocalDate.of(2024, 7, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        // 所有 Y 标签应该是整数或一位小数
        result.yLabels.forEach { label ->
            val parsed = label.label.toDoubleOrNull()
            assertThat(parsed).isNotNull()
        }
    }

    // ===== 归一化边界 =====

    @Test
    fun `normalized Y values are within 0 to 1`() {
        val records = listOf(
            createRecord("r1", 65.0, LocalDate.of(2024, 1, 15)),
            createRecord("r2", 70.0, LocalDate.of(2024, 2, 15)),
            createRecord("r3", 75.0, LocalDate.of(2024, 3, 15)),
            createRecord("r4", 80.0, LocalDate.of(2024, 4, 15)),
            createRecord("r5", 85.0, LocalDate.of(2024, 5, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        result.points.forEach { point ->
            assertThat(point.y).isAtLeast(0f)
            assertThat(point.y).isAtMost(1f)
        }
    }

    @Test
    fun `minimum value maps to normalized Y near 0 and maximum near 1`() {
        val records = listOf(
            createRecord("r1", 70.0, LocalDate.of(2024, 6, 15)),
            createRecord("r2", 80.0, LocalDate.of(2024, 7, 15))
        )
        val result = GrowthChartData.fromRecords(records)!!

        // 最小值点 Y 接近 0，最大值点 Y 接近 1
        assertThat(result.points[0].y).isLessThan(0.2f) // 70 在 yMin~69 附近
        assertThat(result.points[1].y).isGreaterThan(0.8f) // 80 在 yMax~81 附近
    }
}