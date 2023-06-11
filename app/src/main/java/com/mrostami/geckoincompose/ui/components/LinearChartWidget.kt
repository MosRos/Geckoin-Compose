package com.mrostami.geckoincompose.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mrostami.geckoincompose.model.PriceEntry
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.util.RandomEntriesGenerator

@Composable
fun TenDaysLineChart(
    data: List<PriceEntry>,
    modifier: Modifier = Modifier
) {
    val customStepGenerator = RandomEntriesGenerator(
        yRange = IntProgression.fromClosedRange(rangeStart = 25000, rangeEnd = 30000, step = 400),
        xRange = 0..12,
    )
    val composedEntryProducer: ComposedChartEntryModelProducer<ChartEntryModel> = ComposedChartEntryModelProducer(
        ChartEntryModelProducer(customStepGenerator.generateRandomEntries())
    )

    val prices = data.map { it.price }
    val minPrice = if(prices.isEmpty()) 0 else prices.min()
    val maxPrice = if (prices.isEmpty()) 0 else prices.max()
    val entries = data.map { entryOf(x = it.timeStamp.toFloat(), y = it.price.toFloat()) }
    val chartEntryModelProducer = ChartEntryModelProducer(entries)
//    val model = ChartEntryModelProducer(entries)
//    chartEntryModelProducer.setEntries(entries)
    chartEntryModelProducer.setEntries(entries)

    val model = ChartModel(
        entries = listOf(entries),
        minY = minPrice.toFloat(),
        maxY = maxPrice.toFloat(),
        minX = data.minOfOrNull { it.timeStamp }?.toFloat() ?: 0f,
        maxX = data.maxOfOrNull { it.timeStamp }?.toFloat() ?: 0f,
        stackedPositiveY = maxPrice.toFloat() + (maxPrice.toFloat()*0.15).toFloat(),
        stackedNegativeY = minPrice.toFloat() - (maxPrice.toFloat()*0.15).toFloat(),
        xStep = if (data.size < 2) 1f else (data[1].timeStamp - data[0].timeStamp).toFloat(),
        id = 1
    )
    val marker = rememberMarker()
    val PERSISTENT_MARKER_X = 10f
    val color1 = Color(0xFF78A9D0)
    val chartColors = listOf(color1)
    ProvideChartStyle(rememberChartStyle(chartColors = chartColors)) {
        Chart(
            chart = lineChart(persistentMarkers = remember(marker) {mapOf(PERSISTENT_MARKER_X to marker)}),
            model = model,
//            startAxis = startAxis(sizeConstraint = Axis.SizeConstraint.Fraction(0.3f)),
//            bottomAxis = bottomAxis(guideline = null),
            marker = marker,
        )
    }
}

data class ChartModel(
    override val entries: List<List<ChartEntry>>,
    override val minX: Float,
    override val maxX: Float,
    override val minY: Float,
    override val maxY: Float,
    override val stackedPositiveY: Float,
    override val stackedNegativeY: Float,
    override val xStep: Float,
    override val id: Int,
) : ChartEntryModel