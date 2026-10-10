package com.tuantuan.journal.ui.screen.growth

import com.tuantuan.journal.domain.model.GrowthRecord
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.floor

/**
 * 成长曲线图数据模型 — GROWTH_PAGE.md §5
 *
 * 将 GrowthRecord 列表转换为图表绘制所需的归一化数据点。
 * Y轴范围紧贴数据范围（不从0开始），最多5条水平参考线。
 */
data class GrowthChartData(
    /** 归一化数据点列表（x, y 均在 [0, 1] 范围） */
    val points: List<ChartPoint>,
    /** Y轴刻度标签（数值 → 显示文本） */
    val yLabels: List<YLabel>,
    /** X轴刻度标签（月份） */
    val xLabels: List<XLabel>,
    /** Y轴最小值（实际数据范围） */
    val yMin: Float,
    /** Y轴最大值（实际数据范围） */
    val yMax: Float,
    /** 数据点数量 */
    val dataCount: Int
) {
    /** 单个数据点（归一化坐标 + 原始数据） */
    data class ChartPoint(
        val x: Float,       // [0, 1]
        val y: Float,       // [0, 1]
        val value: Double,  // 原始数值
        val date: LocalDate // 测量日期
    )

    /** Y轴刻度标签 */
    data class YLabel(
        val value: Float,   // 实际数值
        val label: String,  // 显示文本
        val normalizedY: Float // [0, 1] 归一化位置
    )

    /** X轴刻度标签 */
    data class XLabel(
        val label: String,  // 显示文本（如 "6月"）
        val normalizedX: Float // [0, 1] 归一化位置
    )

    companion object {
        /** 最大水平参考线数量 — GROWTH_PAGE.md §5.3 */
        const val MAX_GRID_LINES = 5

        /** Y轴上下留白比例 */
        const val Y_PADDING_RATIO = 0.1f

        /** 月份格式化 */
        private val monthFormatter = DateTimeFormatter.ofPattern("M月")

        /**
         * 从成长记录列表构建图表数据。
         *
         * @param records 按日期升序排列的成长记录
         * @return 图表数据，若数据不足返回 null
         */
        fun fromRecords(records: List<GrowthRecord>): GrowthChartData? {
            if (records.isEmpty()) return null

            val sorted = records.sortedBy { it.measureDate }
            val values = sorted.map { it.value }

            // 计算 Y 轴范围（紧贴数据，留少量余量）
            val dataMin = values.min()
            val dataMax = values.max()
            val range = dataMax - dataMin

            // 若所有值相同，人为扩展范围
            val yMin: Float
            val yMax: Float
            if (range < 0.001) {
                val center = dataMin.toFloat()
                yMin = center - 1f
                yMax = center + 1f
            } else {
                val padding = range * Y_PADDING_RATIO
                yMin = (dataMin - padding).toFloat()
                yMax = (dataMax + padding).toFloat()
            }

            // 生成 Y 轴刻度（最多 MAX_GRID_LINES 条）
            val yLabels = generateYLabels(yMin, yMax)

            // 生成数据点
            val points = if (sorted.size == 1) {
                // 单个数据点：放在图表中央
                listOf(
                    ChartPoint(
                        x = 0.5f,
                        y = normalizeValue(sorted[0].value, yMin, yMax),
                        value = sorted[0].value,
                        date = sorted[0].measureDate
                    )
                )
            } else {
                sorted.mapIndexed { index, record ->
                    ChartPoint(
                        x = index.toFloat() / (sorted.size - 1),
                        y = normalizeValue(record.value, yMin, yMax),
                        value = record.value,
                        date = record.measureDate
                    )
                }
            }

            // 生成 X 轴刻度
            val xLabels = generateXLabels(sorted)

            return GrowthChartData(
                points = points,
                yLabels = yLabels,
                xLabels = xLabels,
                yMin = yMin,
                yMax = yMax,
                dataCount = sorted.size
            )
        }

        /** 将实际数值归一化到 [0, 1] */
        private fun normalizeValue(value: Double, yMin: Float, yMax: Float): Float {
            if (yMax == yMin) return 0.5f
            return ((value - yMin) / (yMax - yMin)).toFloat().coerceIn(0f, 1f)
        }

        /** 生成 Y 轴刻度标签（最多 MAX_GRID_LINES 条均匀分布的参考线） */
        private fun generateYLabels(yMin: Float, yMax: Float): List<YLabel> {
            val range = yMax - yMin
            if (range <= 0f) {
                return listOf(YLabel(yMin, formatValue(yMin.toDouble()), 0f))
            }

            // 计算合适的刻度间隔
            val rawStep = range / MAX_GRID_LINES
            val step = niceStep(rawStep)
            val start = ceil(yMin / step) * step

            val labels = mutableListOf<YLabel>()
            var value = start
            while (value <= yMax + step * 0.01f) { // 浮点容差
                val normalizedY = normalizeValue(value.toDouble(), yMin, yMax)
                labels.add(YLabel(value, formatValue(value.toDouble()), normalizedY))
                value += step
            }

            return labels
        }

        /** 生成 X 轴刻度标签 */
        private fun generateXLabels(records: List<GrowthRecord>): List<XLabel> {
            if (records.size == 1) {
                return listOf(
                    XLabel(
                        label = monthFormatter.format(records[0].measureDate),
                        normalizedX = 0.5f
                    )
                )
            }

            return records.mapIndexed { index, record ->
                XLabel(
                    label = monthFormatter.format(record.measureDate),
                    normalizedX = index.toFloat() / (records.size - 1)
                )
            }
        }

        /** 计算"好看的"刻度步长（1, 2, 5 的倍数） */
        private fun niceStep(rawStep: Float): Float {
            val exponent = floor(log10(rawStep.toDouble())).toInt()
            val powerOfTen = Math.pow(10.0, exponent.toDouble()).toFloat()
            val fraction = rawStep / powerOfTen
            val niceFraction = when {
                fraction <= 1f -> 1f
                fraction <= 2f -> 2f
                fraction <= 5f -> 5f
                else -> 10f
            }
            return niceFraction * powerOfTen
        }

        /** 格式化数值显示（整数不显示小数点，否则保留一位） */
        fun formatValue(value: Double): String {
            return if (value == value.toLong().toDouble()) {
                value.toLong().toString()
            } else {
                String.format("%.1f", value)
            }
        }

        private fun log10(x: Double): Double = kotlin.math.log10(x)
    }
}