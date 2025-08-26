package com.mrostami.geckoincompose.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberVerticalLegend
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.runBlocking


private val LegendLabelKey = ExtraStore.Key<Set<String>>()

@Composable
fun TenDaysLineChart(
    rawData: List<PriceEntry>,
    modifier: Modifier = Modifier
) {
    val data = rawData.sortedBy { it.timeStamp }
    if (data.isEmpty()) return
    val max = data.maxBy { it.price }
    val min = data.minBy { it.price }

    val prices = data.map { it.price }
    val minPrice = if (prices.isEmpty()) 0 else prices.min()
    val maxPrice = if (prices.isEmpty()) 0 else prices.max()
    val minX = data.minOfOrNull { it.timeStamp } ?: 0
    val maxX = data.maxOfOrNull { it.timeStamp } ?: 0

    val PERSISTENT_MARKER_X = 10f
    val color1 = Color(0xFF78A9D0)
    val chartColors = listOf(color1)

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/z5ah6v.
            lineSeries { series(data.map { it.timeStamp }, data.map { it.price }) }
//            extras { extraStore -> extraStore[LegendLabelKey] = data.map { it.price.toInt().toString() }.toSet() }
        }
    }
    val lineColors = listOf(Color(0xFF78A9D0))
    val legendItemLabelComponent = rememberTextComponent(GeckoinTheme.customColors.textPrimary)
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                rangeProvider = remember { CartesianLayerRangeProvider.fixed(minX = minX.toDouble(), maxX = maxX.toDouble(), minY = minPrice.toDouble(), maxY = maxPrice.toDouble()) },
                lineProvider = LineCartesianLayer.LineProvider.series(
                    lineColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(color)),
                            areaFill = null,
                            pointProvider =
                                LineCartesianLayer.PointProvider.single(
                                    LineCartesianLayer.point(rememberShapeComponent(fill(color), CorneredShape.Pill))
                                ),
                        )
                    }
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
            marker = rememberMarker(),
            legend = null,
            decorations = listOf(rememberHorizontalLine()),
        ),
        modelProducer,
//        modifier.height(300.dp),
        modifier = modifier,
        rememberVicoScrollState(scrollEnabled = false),
    )
}

@Composable
private fun rememberHorizontalLine(): HorizontalLine {
//    val gradientShader = LinearGradientShaderProvider(
//        colors = intArrayOf(Color(0xFF78A9D0), GeckoinTheme.colorScheme.surface),
//        positions = null,
//        isHorizontal = true
//    )
    val fill = Fill.Transparent
    val line = rememberLineComponent(fill = fill, thickness = 2.dp)
    val labelComponent =
        rememberTextComponent(
            margins = Insets(startDp = 6f),
            padding = Insets(startDp = 8f, topDp = 2f, endDp = 8f, bottomDp = 4f),
            background =
                rememberShapeComponent(
                    fill,
                    CorneredShape.rounded(bottomLeftDp = 4f, bottomRightDp = 4f)
                ),
        )
    return remember {
        HorizontalLine(
            y = { 110000.0 },
            line = line,
            labelComponent = labelComponent,
            label = { "" },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}

@Composable
private fun rememberVerticalLine(min: Double): HorizontalLine {
//    val gradientShader = LinearGradientShaderProvider(
//        colors = intArrayOf(Color(0xFF78A9D0), GeckoinTheme.colorScheme.surface),
//        positions = null,
//        isHorizontal = true
//    )
    val fill = Fill.Transparent
    val line = rememberLineComponent(fill = fill, thickness = 2.dp)
    val labelComponent =
        rememberTextComponent(
            margins = Insets(startDp = 6f),
            padding = Insets(startDp = 8f, topDp = 2f, endDp = 8f, bottomDp = 4f),
            background =
                rememberShapeComponent(
                    fill,
                    CorneredShape.rounded(bottomLeftDp = 4f, bottomRightDp = 4f)
                ),
        )
    return remember {
        HorizontalLine(
            y = { min },
            line = line,
            labelComponent = labelComponent,
            label = { "" },
            verticalLabelPosition = Position.Vertical.Bottom,
        )
    }
}