package com.tuantuan.journal.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.screen.growth.GrowthChartData
import com.tuantuan.journal.ui.screen.growth.GrowthChartData.ChartPoint
import com.tuantuan.journal.ui.screen.growth.GrowthChartData.XLabel
import com.tuantuan.journal.ui.screen.growth.GrowthChartData.YLabel
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * 成长曲线图组件 — GROWTH_PAGE.md §5
 *
 * 设计原则：
 * - 简洁、清晰、无过度网格
 * - 不使用红绿色判断，不显示参考曲线/百分位
 * - Y轴范围紧贴数据范围（不从0开始）
 * - 最多5条水平参考线
 *
 * @param chartData 图表数据，null 时不显示
 * @param unit 单位文本（如 "cm"、"kg"）
 * @param modifier Modifier
 */
@Composable
fun TtGrowthChart(
    chartData: GrowthChartData?,
    unit: String,
    modifier: Modifier = Modifier
) {
    if (chartData == null || chartData.points.isEmpty()) return

    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface
    val captionStyle = MaterialTheme.typography.bodySmall
    val textMeasurer = rememberTextMeasurer()

    // 图表区域尺寸参数
    val chartHeight = 180.dp
    val yAxisWidth = 48.dp
    val xAxisHeight = 24.dp

    // 最小图表宽度（数据点较多时横向滚动）
    val minChartWidth = 280.dp
    val dataPointSpacing = 60.dp
    val chartWidth = if (chartData.dataCount <= 1) {
        minChartWidth
    } else {
        maxOf(minChartWidth, dataPointSpacing * (chartData.dataCount - 1))
    }

    // 点击状态：选中的数据点索引
    var selectedPointIndex by remember { mutableIntStateOf(-1) }

    // 单个数据点提示
    val singlePointHint = stringResource(R.string.growth_curve_single_hint)

    // 预计算像素值（在 Composable 上下文中）
    val density = LocalDensity.current
    val canvasWidthPx = with(density) { chartWidth.toPx() }
    val canvasHeightPx = with(density) { (chartHeight + xAxisHeight + TuantuanSpacing.SM).toPx() }
    val yAxisWidthPx = with(density) { yAxisWidth.toPx() }
    val xAxisHeightPx = with(density) { xAxisHeight.toPx() }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = TuantuanSpacing.SM)
        ) {
            // 横向可滚动图表区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Canvas(
                    modifier = Modifier
                        .requiredWidth(chartWidth)
                        .then(
                            Modifier.pointerInput(chartData) {
                                detectTapGestures { offset ->
                                    selectedPointIndex = findNearestPoint(
                                        tapOffset = offset,
                                        points = chartData.points,
                                        canvasWidth = canvasWidthPx,
                                        canvasHeight = canvasHeightPx,
                                        yAxisWidthPx = yAxisWidthPx,
                                        xAxisHeightPx = xAxisHeightPx
                                    )
                                }
                            }
                        )
                ) {
                    val yAxisWidthPx = yAxisWidth.toPx()
                    val xAxisHeightPx = xAxisHeight.toPx()
                    val smPaddingPx = TuantuanSpacing.SM.toPx()
                    val basePaddingPx = TuantuanSpacing.Base.toPx()

                    // 绘图区域边界
                    val plotLeft = yAxisWidthPx + smPaddingPx
                    val plotTop = smPaddingPx
                    val plotRight = size.width - basePaddingPx
                    val plotBottom = size.height - xAxisHeightPx
                    val plotWidth = plotRight - plotLeft
                    val plotHeight = plotBottom - plotTop

                    // 绘制水平网格线和 Y 轴标签
                    drawGridLines(
                        yLabels = chartData.yLabels,
                        plotLeft = plotLeft,
                        plotRight = plotRight,
                        plotTop = plotTop,
                        plotHeight = plotHeight,
                        gridColor = onSurfaceVariantColor.copy(alpha = 0.08f),
                        textMeasurer = textMeasurer,
                        textStyle = captionStyle,
                        textColor = onSurfaceVariantColor,
                        yAxisWidthPx = yAxisWidthPx
                    )

                    // 绘制 X 轴标签
                    drawXLabels(
                        xLabels = chartData.xLabels,
                        plotLeft = plotLeft,
                        plotWidth = plotWidth,
                        plotBottom = plotBottom,
                        textMeasurer = textMeasurer,
                        textStyle = captionStyle,
                        textColor = onSurfaceVariantColor
                    )

                    // 绘制曲线和数据点
                    val curveColor = primaryColor
                    val pointRadius = 4.dp.toPx()

                    if (chartData.points.size >= 2) {
                        drawSmoothCurve(
                            points = chartData.points,
                            plotLeft = plotLeft,
                            plotTop = plotTop,
                            plotWidth = plotWidth,
                            plotHeight = plotHeight,
                            color = curveColor,
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // 绘制数据点
                    chartData.points.forEachIndexed { index, point ->
                        val cx = plotLeft + point.x * plotWidth
                        val cy = plotTop + (1f - point.y) * plotHeight

                        drawCircle(
                            color = curveColor,
                            radius = pointRadius,
                            center = Offset(cx, cy)
                        )

                        // 选中状态：显示 Tooltip
                        if (index == selectedPointIndex) {
                            drawTooltip(
                                point = point,
                                unit = unit,
                                cx = cx,
                                cy = cy,
                                pointRadius = pointRadius,
                                surfaceColor = surfaceColor,
                                textMeasurer = textMeasurer,
                                textStyle = captionStyle,
                                textColor = onSurfaceVariantColor
                            )
                        }
                    }
                }
            }

            // 单个数据点提示
            if (chartData.dataCount == 1) {
                Text(
                    text = singlePointHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = TuantuanSpacing.Base, vertical = TuantuanSpacing.XS)
                )
            }
        }
    }
}

/** 绘制水平网格线和 Y 轴标签 */
private fun DrawScope.drawGridLines(
    yLabels: List<YLabel>,
    plotLeft: Float,
    plotRight: Float,
    plotTop: Float,
    plotHeight: Float,
    gridColor: Color,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    textColor: Color,
    yAxisWidthPx: Float
) {
    yLabels.forEach { label ->
        val y = plotTop + (1f - label.normalizedY) * plotHeight

        // 水平虚线
        drawLine(
            color = gridColor,
            start = Offset(plotLeft, y),
            end = Offset(plotRight, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
        )

        // Y 轴标签
        val textLayoutResult = textMeasurer.measure(
            text = label.label,
            style = textStyle.copy(color = textColor)
        )
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                yAxisWidthPx - textLayoutResult.size.width - TuantuanSpacing.XS.toPx(),
                y - textLayoutResult.size.height / 2f
            )
        )
    }
}

/** 绘制 X 轴标签 */
private fun DrawScope.drawXLabels(
    xLabels: List<XLabel>,
    plotLeft: Float,
    plotWidth: Float,
    plotBottom: Float,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    textColor: Color
) {
    // 当标签过多时，只显示部分标签避免重叠
    val maxLabels = 8
    val step = if (xLabels.size > maxLabels) {
        ceil(xLabels.size.toFloat() / maxLabels).toInt()
    } else {
        1
    }

    xLabels.forEachIndexed { index, label ->
        if (index % step != 0 && index != xLabels.lastIndex) return@forEachIndexed

        val x = plotLeft + label.normalizedX * plotWidth
        val textLayoutResult = textMeasurer.measure(
            text = label.label,
            style = textStyle.copy(color = textColor)
        )
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x - textLayoutResult.size.width / 2f,
                plotBottom + TuantuanSpacing.XS.toPx()
            )
        )
    }
}

/** 绘制平滑曲线（使用贝塞尔曲线） */
private fun DrawScope.drawSmoothCurve(
    points: List<ChartPoint>,
    plotLeft: Float,
    plotTop: Float,
    plotWidth: Float,
    plotHeight: Float,
    color: Color,
    strokeWidth: Float
) {
    if (points.size < 2) return

    val path = Path()
    val offsets = points.map { point ->
        Offset(
            plotLeft + point.x * plotWidth,
            plotTop + (1f - point.y) * plotHeight
        )
    }

    path.moveTo(offsets.first().x, offsets.first().y)

    for (i in 1 until offsets.size) {
        val prev = offsets[i - 1]
        val curr = offsets[i]
        val controlX = (prev.x + curr.x) / 2f
        path.cubicTo(
            controlX, prev.y,
            controlX, curr.y,
            curr.x, curr.y
        )
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidth)
    )
}

/** 绘制数据点 Tooltip */
private fun DrawScope.drawTooltip(
    point: ChartPoint,
    unit: String,
    cx: Float,
    cy: Float,
    pointRadius: Float,
    surfaceColor: Color,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    textColor: Color
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val valueText = "${GrowthChartData.formatValue(point.value)} $unit"
    val dateText = dateFormatter.format(point.date)

    val valueLayout = textMeasurer.measure(text = valueText, style = textStyle.copy(color = textColor))
    val dateLayout = textMeasurer.measure(text = dateText, style = textStyle.copy(color = textColor))

    val tooltipWidth = maxOf(valueLayout.size.width, dateLayout.size.width) + TuantuanSpacing.Base.toPx() * 2
    val tooltipHeight = (valueLayout.size.height + dateLayout.size.height).toFloat() +
            TuantuanSpacing.SM.toPx() * 2 + TuantuanSpacing.Base.toPx()

    // Tooltip 位置（在数据点上方）
    val tooltipX = (cx - tooltipWidth / 2f).coerceIn(0f, size.width - tooltipWidth)
    val tooltipY = (cy - pointRadius - tooltipHeight - TuantuanSpacing.XS.toPx()).coerceAtLeast(0f)

    // 背景
    drawRoundRect(
        color = surfaceColor,
        topLeft = Offset(tooltipX, tooltipY),
        size = Size(tooltipWidth, tooltipHeight),
        cornerRadius = CornerRadius(TuantuanSpacing.SM.toPx())
    )

    // 数值文本
    drawText(
        textLayoutResult = valueLayout,
        topLeft = Offset(
            tooltipX + (tooltipWidth - valueLayout.size.width) / 2f,
            tooltipY + TuantuanSpacing.SM.toPx()
        )
    )

    // 日期文本
    drawText(
        textLayoutResult = dateLayout,
        topLeft = Offset(
            tooltipX + (tooltipWidth - dateLayout.size.width) / 2f,
            tooltipY + TuantuanSpacing.SM.toPx() + valueLayout.size.height + TuantuanSpacing.XS.toPx()
        )
    )
}

/** 查找点击位置最近的数据点索引 */
private fun findNearestPoint(
    tapOffset: Offset,
    points: List<ChartPoint>,
    canvasWidth: Float,
    canvasHeight: Float,
    yAxisWidthPx: Float,
    xAxisHeightPx: Float
): Int {
    val smPadding = TuantuanSpacing.SM.value * 1f // 近似 dp to px
    val basePadding = TuantuanSpacing.Base.value * 1f

    val plotLeft = yAxisWidthPx + smPadding
    val plotRight = canvasWidth - basePadding
    val plotTop = smPadding
    val plotBottom = canvasHeight - xAxisHeightPx
    val plotWidth = plotRight - plotLeft
    val plotHeight = plotBottom - plotTop

    var nearestIndex = -1
    var nearestDist = Float.MAX_VALUE
    val tapRadius = 48f // 点击检测半径（px）

    points.forEachIndexed { index, point ->
        val px = plotLeft + point.x * plotWidth
        val py = plotTop + (1f - point.y) * plotHeight
        val dx = tapOffset.x - px
        val dy = tapOffset.y - py
        val dist = sqrt(dx * dx + dy * dy)
        if (dist < tapRadius && dist < nearestDist) {
            nearestDist = dist
            nearestIndex = index
        }
    }

    return nearestIndex
}