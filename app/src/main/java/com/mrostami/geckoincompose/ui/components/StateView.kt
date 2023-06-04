package com.mrostami.geckoincompose.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mrostami.geckoincompose.ui.base.BaseUiModel
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun StateView(
    uiModel: BaseUiModel,
    retryOnError: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Crossfade(targetState = uiModel, modifier = modifier, label = "") {
        when (it.state) {
            BaseUiModel.State.LOADING -> CircularLoadingIndicator()
            BaseUiModel.State.ERROR -> ErrorView(message = it.errorMessage, retryAction = retryOnError)
            BaseUiModel.State.SUCCESS -> content()
        }
    }
}

@Composable
fun CircularLoadingIndicator() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    CircularProgressIndicator(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.size(40.dp)
    )
}

@Composable
fun ErrorView(
    message: String? = "Oops! An Error Occurred!",
    retryAction: () -> Unit = {}
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message ?: "Oops! An Error Occurred!",
            color = GeckoinTheme.colorScheme.error,
            style = GeckoinTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(Dp(12f)))
        TextButton(onClick = retryAction) {
            Text(
                text = "Retry",
                color = GeckoinTheme.customColors.textSecondary,
                style = GeckoinTheme.typography.bodyMedium
            )
        }
    }
}
