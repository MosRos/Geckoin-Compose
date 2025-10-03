package com.mrostami.geckoincompose.ui.components

import android.icu.text.DecimalFormat
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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

    var selectedPointIndex by remember { mutableStateOf(-1) }
    val touchRadiusPx = tapSensitivityRadius.value
    val textMeasurer = rememberTextMeasurer()
    val priceTextStyle = TextStyle(
        color = priceValueColor,
        fontSize = priceValueFontSize.value.sp
    )
    val priceValueOffsetY = priceValueOffset.value
    val priceFormatter = remember { DecimalFormat("#,##0.##") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4 / 3f)
    ) {
        val chartWidth = constraints.maxWidth.toFloat()
        val chartHeight = constraints.maxHeight.toFloat()
        val yPadding = chartHeight * 0.1f // Define yPadding here for consistent use
        val canvasSize = Size(chartWidth, chartHeight) // Define canvasSize here

        var initialAnimationDone by rememberSaveable { mutableStateOf(false) }
        var pointsOffset by remember {
            mutableStateOf<List<ProcessedChartPoint>>(emptyList())
        }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(data) {
            if (chartWidth == 0f || chartHeight == 0f) {
                isLoading = true
                return@LaunchedEffect
            }
            isLoading = true

            if (!initialAnimationDone) {
                coroutineScope {
                    launch { horizontalRevealProgress.snapTo(0f) }
                    launch { verticalPositionProgress.snapTo(0f) }
                }
            }

            val result = withContext(Dispatchers.Default) {
                if (data.isEmpty()) {
                    return@withContext emptyList<ProcessedChartPoint>() // Return empty list
                }
                val prices = data.map { it.price }
                val timestamps = data.map { it.timeStamp }

                val minP = prices.minOrNull() ?: 0.0
                val maxP = prices.maxOrNull() ?: 0.0
                val minT = timestamps.minOrNull() ?: 0L
                val maxT = timestamps.maxOrNull() ?: 0L

                val xRange = (maxT - minT).toFloat()
                val yRange = (maxP - minP).toFloat()
                val middleY = chartHeight / 2f

                data.mapIndexed { index, entry ->
                    val realOffset = getOffset(
                        entry = entry,
                        maxTimestamp = maxT,
                        minTimestamp = minT,
                        maxPrice = maxP,
                        minPrice = minP,
                        xRange = xRange,
                        yRange = yRange,
                        size = canvasSize, // Use predefined canvasSize
                        yPadding = yPadding  // Use predefined yPadding
                    )
                    val initialFlatOffset = if (!initialAnimationDone) {
                        Offset(realOffset.x, middleY) // Animate from middle if first time
                    } else {
                        realOffset // Go directly to realOffset if already animated
                    }
                    ProcessedChartPoint(
                        priceEntry = entry,
                        startOffset = initialFlatOffset, // This will be realOffset after initial animation
                        endOffset = realOffset,
                        index = index
                    )
                }
            }

            pointsOffset = result
            isLoading = false
            selectedPointIndex = -1 // Reset selection on any data change

            if (!initialAnimationDone) {
                coroutineScope {
                }
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
                            delayMillis = 700, // Consider if this delay is needed after initial animation
                            durationMillis = animationDurationMillis,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
                initialAnimationDone = true
            } else {
                // If animation is done, snap progress to final state for immediate update
                horizontalRevealProgress.snapTo(1f)
                verticalPositionProgress.snapTo(1f)
            }
        }

        if (isLoading && pointsOffset.isEmpty() && !initialAnimationDone) { // Show loader only during initial load
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center),
                color = GeckoinTheme.colorScheme.primary
            )
        } else if (pointsOffset.isNotEmpty()) {
            val currentFrameAnimatedData = remember(
                pointsOffset,
                horizontalRevealProgress.value,
                verticalPositionProgress.value,
                initialAnimationDone
            ) {
                val animatedPointsCount =
                    if (initialAnimationDone && horizontalRevealProgress.value < 1f) {
                        // If initial animation was fast-forwarded due to data update, ensure all points are shown
                        pointsOffset.size
                    } else {
                        (pointsOffset.size * horizontalRevealProgress.value).toInt()
                    }

                pointsOffset.take(animatedPointsCount).map { chartPoint ->
                    // If initial animation is done, currentY should be endOffset.y directly.
                    // Otherwise, interpolate for the animation.
                    val currentY = if (initialAnimationDone) {
                        chartPoint.endOffset.y
                    } else {
                        lerp(
                            chartPoint.startOffset.y,
                            chartPoint.endOffset.y,
                            verticalPositionProgress.value
                        )
                    }
                    // The X should always be the final X after the horizontal reveal,
                    // or immediately if the initial animation is done.
                    val currentX =
                        chartPoint.endOffset.x // X is determined by data, not vertical animation
                    val currentOffset = Offset(currentX, currentY)
                    chartPoint.copy(currentDisplayOffset = currentOffset)
                }
            }

            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(currentFrameAnimatedData, touchRadiusPx) {
                        detectTapGestures(
                            onTap = { tapOffset ->
                                var newSelectedPointIndex = -1
                                var minDistance = Float.MAX_VALUE

                                currentFrameAnimatedData.forEach { chartPointData -> // Iterate directly over potentially full list
                                    chartPointData.currentDisplayOffset?.let { pointCenter ->
                                        val dx = tapOffset.x - pointCenter.x
                                        val dy = tapOffset.y - pointCenter.y
                                        val distanceSquared = dx.pow(2) + dy.pow(2)
                                        val distance = sqrt(distanceSquared)

                                        if (distance < touchRadiusPx && distance < minDistance) {
                                            minDistance = distance
                                            newSelectedPointIndex = chartPointData.index
                                        }
                                    }
                                }
                                selectedPointIndex =
                                    if (newSelectedPointIndex == selectedPointIndex) -1 else newSelectedPointIndex
                            }
                        )
                    }
            ) {
                if (currentFrameAnimatedData.isEmpty()) return@Canvas
                val currentFramePoints =
                    currentFrameAnimatedData.mapNotNull { it.currentDisplayOffset }
                if (currentFramePoints.isEmpty()) return@Canvas

                // --- 1. Draw the Fading Gradient ---
                val gradientPath = Path()
                if (currentFramePoints.isNotEmpty()) {
                    gradientPath.moveTo(currentFramePoints.first().x, chartHeight - yPadding)
                    gradientPath.lineTo(currentFramePoints.first().x, currentFramePoints.first().y)
                    for (i in 0 until currentFramePoints.size - 1) {
                        val (control1, control2) = calculateControlPoints(
                            currentFramePoints,
                            i,
                            smoothness
                        )
                        gradientPath.cubicTo(
                            control1.x,
                            control1.y,
                            control2.x,
                            control2.y,
                            currentFramePoints[i + 1].x,
                            currentFramePoints[i + 1].y
                        )
                    }
                    gradientPath.lineTo(currentFramePoints.last().x, chartHeight - yPadding)
                    gradientPath.close()
                    drawPath(
                        path = gradientPath,
                        brush = Brush.verticalGradient(
                            listOf(gradientStartColor, gradientEndColor),
                            startY = currentFramePoints.minOfOrNull { it.y } ?: 0f,
                            endY = chartHeight - yPadding))
                }

                // --- 2. Draw the Line Connecting Dots ---
                val linePath = Path()
                if (currentFramePoints.isNotEmpty()) {
                    linePath.moveTo(currentFramePoints.first().x, currentFramePoints.first().y)
                    if (currentFramePoints.size == 2) {
                        linePath.lineTo(currentFramePoints[1].x, currentFramePoints[1].y)
                    } else if (currentFramePoints.size > 2) {
                        for (i in 0 until currentFramePoints.size - 1) {
                            val (control1, control2) = calculateControlPoints(
                                currentFramePoints,
                                i,
                                smoothness
                            )
                            linePath.cubicTo(
                                control1.x,
                                control1.y,
                                control2.x,
                                control2.y,
                                currentFramePoints[i + 1].x,
                                currentFramePoints[i + 1].y
                            )
                        }
                    }
                    drawPath(
                        path = linePath,
                        color = lineColor,
                        style = Stroke(lineWidth.toPx(), cap = StrokeCap.Round)
                    )
                }

                // --- 3. Draw the Dots & Price for Selected Point ---
                currentFrameAnimatedData.forEach { chartPointData ->
                    chartPointData.currentDisplayOffset?.let { pointOffset ->
                        val isSelected = chartPointData.index == selectedPointIndex
                        val currentDotRadius =
                            if (isSelected) dotRadius.toPx() * 2 else dotRadius.toPx()

                        drawCircle(
                            color = if (isSelected) priceValueColor else dotColor,
                            radius = currentDotRadius,
                            center = pointOffset,
                            alpha = if (isSelected) 1f else 0.8f
                        )

                        if (isSelected && showPriceValues) { // check showPriceValues flag
                            val priceText = priceFormatter.format(chartPointData.priceEntry.price)
                            val textLayoutResult: TextLayoutResult = textMeasurer.measure(
                                AnnotatedString(priceText), style = priceTextStyle
                            )
                            val textX = pointOffset.x - (textLayoutResult.size.width / 2)
                            // Adjust textY to ensure it's above the dot, considering the dot's current radius
                            val textY =
                                pointOffset.y - currentDotRadius - priceValueOffsetY - textLayoutResult.size.height
                            drawText(textLayoutResult, topLeft = Offset(textX, textY))
                        }
                    }
                }
            }
        }
    }
}

// Data class for processed points, remains the same
data class ProcessedChartPoint(
    val priceEntry: PriceEntry,
    val startOffset: Offset, // Initial position for animation (e.g., center line or previous position)
    val endOffset: Offset,   // Final calculated position on the chart
    val index: Int,
    val currentDisplayOffset: Offset? = null // The offset currently being rendered (could be animated)
)

// Helper function getOffset, remains the same
fun getOffset(
    entry: PriceEntry,
    maxTimestamp: Long,
    minTimestamp: Long,
    maxPrice: Double,
    minPrice: Double,
    xRange: Float,
    yRange: Float,
    size: Size,
    yPadding: Float
): Offset {
    val x = if (xRange == 0f) {
        size.width / 2f // Center if only one point
    } else {
        ((entry.timeStamp - minTimestamp) / xRange) * size.width
    }

    val y = if (yRange == 0f) {
        (size.height - 2 * yPadding) / 2f + yPadding // Center vertically if all prices are same
    } else {
        size.height - (((entry.price - minPrice).toFloat() / yRange) * (size.height - 2 * yPadding) + yPadding)
    }
    return Offset(x.toFloat(), y.toFloat())
}

// Helper function calculateControlPoints, remains the same
fun calculateControlPoints(
    points: List<Offset>,
    index: Int,
    smoothness: Float
): Pair<Offset, Offset> {
    val p0 = points.getOrElse(index - 1) { points[index] }
    val p1 = points[index]
    val p2 = points[index + 1]
    val p3 = points.getOrElse(index + 2) { p2 }

    val control1X = p1.x + (p2.x - p0.x) * smoothness
    val control1Y = p1.y + (p2.y - p0.y) * smoothness
    val control2X = p2.x - (p3.x - p1.x) * smoothness
    val control2Y = p2.y - (p3.y - p1.y) * smoothness

    return Offset(control1X, control1Y) to Offset(control2X, control2Y)
}
