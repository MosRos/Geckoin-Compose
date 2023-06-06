package com.mrostami.geckoincompose.ui.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mrostami.geckoincompose.R
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GeckoinTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val imageSize = (maxWidth / 3)
            Image(
                painter = painterResource(id = R.drawable.bitcoin_logo),
                contentDescription = "app icon",
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(size = 16.dp))
                    .padding(top = 32.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = GeckoinTheme.typography.titleMedium,
                color = GeckoinTheme.customColors.textPrimary,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.Center)
            )
            SettingsOptions(
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun SettingsOptions(
    modifier: Modifier = Modifier
) {
    Column {
        SettingOptionItem(title = "Theme Mode", iconResId = R.drawable.ic_sun) {
            // TODO: open theme settings dialog
        }
        Divider(
            thickness = 1.dp,
            color = GeckoinTheme.customColors.dividerColor,
            startIndent = 70.dp
        )
        SettingOptionItem(title = "About Developer", iconResId = R.drawable.ic_code) {
            // TODO: open developer LinkedIn
//            openLinkInBrowser()
        }
        Divider(
            thickness = 1.dp,
            color = GeckoinTheme.customColors.dividerColor,
            startIndent = 70.dp
        )
        SettingOptionItem(title = "About", iconResId = R.drawable.ic_info)
    }
}

@Composable
fun SettingOptionItem(
    title: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    action: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable { action.invoke() },
//            .padding(horizontal = 16.dp),
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "option icon",
            tint = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier
                .size(24.dp)
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = title,
            style = GeckoinTheme.typography.bodyMedium,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
        )
    }
}