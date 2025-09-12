package com.mrostami.geckoincompose.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import com.mrostami.geckoincompose.utils.round

@Composable
fun PieChart(
    data: Map<String, Double>,
    radiusOuter: Dp = Dp(340f),
    chartBarWidth: Dp = Dp(20f),
    animDuration: Int = 700,
    modifier: Modifier = Modifier
) {

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()
    var textOffsets = mutableListOf<Offset>()

    // To set the value of each Arc according to
    // the value given in the data, we have used a simple formula.
    // For a detailed explanation check out the Medium Article.
    // The link is in the about section and readme file of this GitHub Repository
    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    // add the colors as per the number of data(no. of pie chart entries)
    // so that each data will get a color
    val colors = listOf(
        Color(0xFF5AAFF0),
        Color(0xFFF37953),
        Color(0xFF9A65F7),
        Color(0xFFEF5146),
        Color(0xFFF0DE3D),
        Color(0xFF3AF0DE),
        Color(0xFF34F33A),
        Color(0xFFF0DD36),
        Color(0xFF29D7ED),
        Color(0xFFDE9E3E),
        Color(0xFFE52E83),
        Color(0xFF3550E1),
        Color(0xFFEB3A76),
        Color(0xFF41E348),
        Color(0xFF309FD2),
        Color(0xFFE15024),
        Color(0xFF419EE5),
        Color(0xFFD05F3C),
        Color(0xFFB18DF1),
        Color(0xFFF07F77),
        Color(0xFFD9C72E),
        Color(0xFF41E4D4),
        Color(0xFFF37953),
        Color(0xFF9A65F7),
        Color(0xFFEF5146),
        Color(0xFF988C1E),
        Color(0xFF349537)
    )

    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    // it is the diameter value of the Pie
//    val animateSize by animateFloatAsState(
//        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
//        animationSpec = tween(
//            durationMillis = animDuration,
//            delayMillis = 0,
//            easing = LinearOutSlowInEasing
//        ), label = ""
//    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 3f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 600,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dp(12f)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            data.toList().forEachIndexed { index, value ->
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = colors[index])
                            .padding(horizontal = Dp(0f), vertical = Dp(3f))
                            .wrapContentHeight()
                            .width(Dp(38f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.first.toString().uppercase(),
                            color = Color.Black,
                            style = GeckoinTheme.typography.labelSmall
                        )
                    }
                    Text(
                        text = " " + value.second.round(decimals = 1).toString(),
                        color = GeckoinTheme.colorScheme.onBackground,
                        style = GeckoinTheme.typography.labelSmall
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Pie Chart using Canvas Arc
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val pieChartSize = min(maxWidth, radiusOuter)
            Canvas(
                modifier = Modifier
                    .size(pieChartSize)
                    .padding(24.dp)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

    }

}