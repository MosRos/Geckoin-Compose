package com.mrostami.geckoincompose.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun TrendCoinItemView(
    coin: TrendCoin,
    modifier: Modifier = Modifier
) {
    ConstraintLayout {
        val (image, symbol, name, rank, divider) = createRefs()

        AsyncImage(
            model = coin.small,
            contentDescription = "bitcoin",
            modifier = Modifier
                .size(Dp(40f))
                .clip(CircleShape)
                .constrainAs(image) {
                    start.linkTo(parent.start, Dp(8f))
                    top.linkTo(parent.top, Dp(8f))
                    bottom.linkTo(parent.bottom, Dp(8f))

                }
        )
        Text(
            text = coin.symbol?.uppercase() ?: "",
            style = GeckoinTheme.typography.bodyLarge,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier.constrainAs(symbol) {
                start.linkTo(image.end, Dp(10f))
                top.linkTo(parent.top, Dp(8f))
            }
        )
        Text(
            text = coin.name ?: "",
            style = GeckoinTheme.typography.bodyMedium,
            color = GeckoinTheme.customColors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(symbol.start)
                top.linkTo(symbol.bottom, Dp(5f))
                bottom.linkTo(parent.bottom, Dp(8f))
            }
        )

        Text(
            text = "#" + coin.marketCapRank.toString(),
            style = GeckoinTheme.typography.titleMedium,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier.constrainAs(rank) {
                end.linkTo(parent.end, Dp(10f))
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        Divider(
            thickness = Dp(1f),
            color = GeckoinTheme.customColors.dividerColor,
            modifier = Modifier.constrainAs(divider){
                start.linkTo(parent.start, Dp(32f))
                end.linkTo(parent.end, Dp(32f))
                bottom.linkTo(parent.bottom)
            }
        )
    }
}