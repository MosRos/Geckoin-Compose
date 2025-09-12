package com.mrostami.geckoincompose.ui.components

import android.icu.text.DecimalFormat
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun AnimatedLineChart(
    modifier: Modifier = Modifier,
    data: List<PriceEntry>,
    lineColor: Color = Color.Blue,
    lineWidth: Dp = 2.dp,
    dotColor: Color = Color.Blue,
    dotRadius: Dp = 4.dp,
    gradientStartColor: Color = lineColor.copy(alpha = 0.63f),
    gradientEndColor: Color = Color.Transparent,
    animationDurationMillis: Int = 700,
    smoothness: Float = 0.2f, // Factor to control curve smoothness (0.0 for st
    showPriceValues: Boolean = true, // Control whether to show prices
    priceValueColor: Color = GeckoinTheme.colorScheme.onBackground,
    priceValueFontSize: Dp = 11.dp,
    priceValueOffset: Dp = 24.dp, // Offset from the dot
    tapSensitivityRadius: Dp = 24.dp
) {
    if (data.isEmpty()) {
        return
    }

    val horizontalRevealProgress = remember { Animatable(0f) }
    val verticalPositionProgress = remember { Animatable(0f) }


    // State to hold the index of the selected point, -1 if none selected
    var selectedPointIndex by remember { mutableStateOf(-1) }
    val touchRadiusPx = tapSensitivityRadius.value

    // For drawing text
    val textMeasurer = rememberTextMeasurer()
    val priceTextStyle = TextStyle(
        color = priceValueColor,
        fontSize = priceValueFontSize.value.sp // Convert Dp to TextUnit
    )
    val priceValueOffsetY = priceValueOffset.value // Use Dp.value for calculations

    // Formatter for price values
    val priceFormatter = remember { DecimalFormat("#,##0.##") }

    BoxWithConstraints(modifier = modifier) {
        val chartWidth = constraints.maxWidth.toFloat()
        val chartHeight = constraints.maxHeight.toFloat()

        val yPadding = chartHeight * 0.1f
        val canvasSize = Size(chartWidth, chartHeight)

        // Pair: (startOffset for Y anim, endOffset for Y anim)
        var pointsOffset by remember {
            mutableStateOf<List<ProcessedChartPoint>>(emptyList())
        }
        var isLoading by remember { mutableStateOf(true) } //

        LaunchedEffect(data, chartWidth, chartHeight) {
            if (chartWidth == 0f || chartHeight == 0f) { // Ensure dimensions are available
                isLoading = true // still waiting for size
                return@LaunchedEffect
            }
            isLoading = true
            pointsOffset = emptyList()
            selectedPointIndex = -1

            coroutineScope {
                launch { horizontalRevealProgress.snapTo(0f) }
                launch { verticalPositionProgress.snapTo(0f) }
            }

            val result = withContext(Dispatchers.Default) {
                if (data.isEmpty()) {
                    return@withContext null // Or some default state
                }
                val prices = data.map { it.price }
                val timestamps = data.map { it.timeStamp }

                val minP = prices.minOrNull() ?: 0.0
                val maxP = prices.maxOrNull() ?: 0.0
                val minT = timestamps.minOrNull() ?: 0L
                val maxT = timestamps.maxOrNull() ?: 0L

                val yPadding = chartHeight * 0.1f
                val canvasSize = Size(chartWidth, chartHeight)

                val xRange = (maxT - minT).toFloat()
                val yRange = (maxP - minP).toFloat()

                val middleY = chartHeight / 2f // Or (chartHeight - 2 * yPadding) / 2f +

                data.mapIndexed { index, entry ->
                    val realOffset = getOffset(
                        entry = entry,
                        maxTimestamp = maxT,
                        minTimestamp = minT,
                        maxPrice = maxP,
                        minPrice = minP,
                        xRange = xRange,
                        yRange = yRange,
                        size = canvasSize,
                        yPadding = yPadding
                    )
                    val initialFlatOffset = Offset(realOffset.x, middleY)
                    ProcessedChartPoint(
                        priceEntry = entry,
                        startOffset = initialFlatOffset,
                        endOffset = realOffset,
                        index = index
                    )
                }
            }

            result?.let { points ->
                pointsOffset = points
                isLoading = false

                // Option 2: Parallel
                coroutineScope {
                    launch {
                        horizontalRevealProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 150, easing = LinearEasing)
                        )
                    }
                    launch {
                        verticalPositionProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                delayMillis = 700,
                                durationMillis = animationDurationMillis,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }

            } ?: run {
                isLoading = false // No data or error in processing
                pointsOffset = emptyList()
            }

        }

        if (isLoading && pointsOffset.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center),
                color = GeckoinTheme.colorScheme.primary
            )
        } else if (pointsOffset.isNotEmpty()) {

            val currentFrameAnimatedData = remember(pointsOffset, horizontalRevealProgress.value, verticalPositionProgress.value) {
                val animatedPointsCount = (pointsOffset.size * horizontalRevealProgress.value).toInt()
                pointsOffset.take(animatedPointsCount).map { chartPoint ->
                    val currentY = lerp(chartPoint.startOffset.y, chartPoint.endOffset.y, verticalPositionProgress.value)
                    val currentOffset = Offset(chartPoint.startOffset.x, currentY)
                    chartPoint.copy(currentDisplayOffset = currentOffset)
                }
            }

            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(
                        currentFrameAnimatedData,
                        touchRadiusPx
                    ) { // Pass animated data for hit testing
                        detectTapGestures(
                            onTap = { tapOffset ->
                                var newSelectedPointIndex = -1
                                var minDistance = Float.MAX_VALUE

                                currentFrameAnimatedData.forEachIndexed { index, chartPointData ->
                                    chartPointData.currentDisplayOffset?.let { pointCenter ->
                                        val dx = tapOffset.x - pointCenter.x
                                        val dy = tapOffset.y - pointCenter.y
                                        val distanceSquared = dx.pow(2) + dy.pow(2)
                                        val distance = sqrt(distanceSquared)

                                        if (distance < touchRadiusPx && distance < minDistance) {
                                            minDistance = distance
                                            newSelectedPointIndex =
                                                chartPointData.index // Use original index
                                        }
                                    }
                                }

                                selectedPointIndex =
                                    if (newSelectedPointIndex == selectedPointIndex) {
                                        -1 // Tap same point again to deselect
                                    } else {
                                        newSelectedPointIndex
                                    }
                            }
                        )
                    }
            ) {

                if (currentFrameAnimatedData.isEmpty()) return@Canvas
                val currentFramePoints = currentFrameAnimatedData.mapNotNull { it.currentDisplayOffset }
                if (currentFramePoints.isEmpty()) return@Canvas


                // --- 1. Draw the Fading Gradient ---
                val gradientPath = Path()
                // ... gradient path logic ... (using currentFramePoints)
                if (currentFramePoints.isNotEmpty()) {
                    gradientPath.moveTo(currentFramePoints.first().x, chartHeight - yPadding)
                    gradientPath.lineTo(currentFramePoints.first().x, currentFramePoints.first().y)
                    for (i in 0 until currentFramePoints.size - 1) {
                        val (control1, control2) = calculateControlPoints(currentFramePoints, i, smoothness)
                        gradientPath.cubicTo(control1.x, control1.y, control2.x, control2.y, currentFramePoints[i + 1].x, currentFramePoints[i + 1].y)
                    }
                    gradientPath.lineTo(currentFramePoints.last().x, chartHeight - yPadding)
                    gradientPath.close()
                    drawPath(path = gradientPath, brush = Brush.verticalGradient(listOf(gradientStartColor, gradientEndColor), startY = currentFramePoints.minOfOrNull { it.y } ?: 0f, endY = chartHeight - yPadding))
                }

                // --- 2. Draw the Line Connecting Dots ---
                val linePath = Path()
                // ... line path logic ... (using currentFramePoints)
                if (currentFramePoints.isNotEmpty()) {
                    linePath.moveTo(currentFramePoints.first().x, currentFramePoints.first().y)
                    if (currentFramePoints.size == 2) {
                        linePath.lineTo(currentFramePoints[1].x, currentFramePoints[1].y)
                    } else if (currentFramePoints.size > 2) {
                        for (i in 0 until currentFramePoints.size - 1) {
                            val (control1, control2) = calculateControlPoints(currentFramePoints, i, smoothness)
                            linePath.cubicTo(control1.x, control1.y, control2.x, control2.y, currentFramePoints[i + 1].x, currentFramePoints[i + 1].y)
                        }
                    }
                    drawPath(path = linePath, color = lineColor, style = Stroke(lineWidth.toPx(), cap = StrokeCap.Round))
                }



                // --- 3. Draw the Dots & Price for Selected Point ---
                currentFrameAnimatedData.forEach { chartPointData ->
                    chartPointData.currentDisplayOffset?.let { pointOffset ->
                        val isSelected = chartPointData.index == selectedPointIndex
                        val currentDotRadius = if (isSelected) dotRadius.toPx()*2 else dotRadius.toPx()

                        drawCircle(
                            color = if (isSelected) priceValueColor else dotColor, // Could use a different color for selected dot
                            radius = currentDotRadius,
                            center = pointOffset,
                            alpha = if (isSelected) 1f else 0.8f // Example: Highlight selected
                        )

                        // Draw price text ONLY for the selected point
                        if (isSelected) {
                            val priceText = priceFormatter.format(chartPointData.priceEntry.price)
                            val textLayoutResult: TextLayoutResult = textMeasurer.measure(
                                AnnotatedString(priceText), style = priceTextStyle
                            )
                            val textX = pointOffset.x - (textLayoutResult.size.width / 2)
                            val textY = pointOffset.y - currentDotRadius - priceValueOffsetY - textLayoutResult.size.height
                            drawText(textLayoutResult, topLeft = Offset(textX, textY))
                        }
                    }
                }
            }
        }
    }
}

// Data class to hold all info needed per point
data class ProcessedChartPoint(
    val priceEntry: PriceEntry,
    val startOffset: Offset, // Initial (flat middle) position
    val endOffset: Offset,   // Final real position
    val currentDisplayOffset: Offset? = null,
    val index: Int
)

// Function to map data points to Canvas coordinates
fun getOffset(
    entry: PriceEntry,
    maxTimestamp: Long,
    minTimestamp: Long,
    xRange: Float = (maxTimestamp - minTimestamp).toFloat(),
    maxPrice: Double,
    minPrice: Double,
    yRange: Float = (maxPrice - minPrice).toFloat(),
    size: androidx.compose.ui.geometry.Size,
    yPadding: Float
): Offset {
//    val xRange = (maxTimestamp - minTimestamp).toFloat()
//    val yRange = (maxPrice - minPrice).toFloat()

    val x = if (xRange == 0f) 0f else ((entry.timeStamp - minTimestamp) / xRange) * size.width
    val yValue = if (yRange == 0f) {
        (size.height - 2 * yPadding) / 2f // Center if all y values are same
    } else {
        ((entry.price - minPrice) / yRange) * (size.height - 2 * yPadding)
    }
    // Invert Y because Canvas Y is 0 at top, increasing downwards
    val y = (size.height - yPadding) - yValue.toFloat()
    return Offset(x.toFloat(), y.toFloat())
}

fun calculateControlPoints(points: List<Offset>, i: Int, smoothness: Float): Pair<Offset, Offset> {
    val p0 = points.getOrElse(i - 1) { points[i] } // Previous point or current if first
    val p1 = points[i]                               // Current point (start of segment)
    val p2 = points[i + 1]                           // Next point (end of segment)
    val p3 = points.getOrElse(i + 2) { points[i + 1] } // Point after next or next if last

    // Calculate tangent lengths
    val d1 = (p2.x - p0.x) * smoothness
    val d2 = (p3.x - p1.x) * smoothness

    val control1 = Offset(p1.x + d1, p1.y) // Adjust y as well for more complex curves if needed
    val control2 = Offset(p2.x - d2, p2.y) // Adjust y as well

    // A more robust method (Catmull-Rom to Bezier control points):
    // Tension factor, typically 0.5f for Catmull-Rom
    val t = 0.5f
    val cp1x = p1.x + (p2.x - p0.x) * t / 3
    val cp1y = p1.y + (p2.y - p0.y) * t / 3
    val cp2x = p2.x - (p3.x - p1.x) * t / 3
    val cp2y = p2.y - (p3.y - p1.y) * t / 3

    return Pair(Offset(cp1x, cp1y), Offset(cp2x, cp2y))
}
