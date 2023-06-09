package com.mrostami.geckoincompose.ui.settings

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mrostami.geckoincompose.R
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val imageSize = (maxWidth / 3)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GeckoinTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.size(80.dp))
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground), // painterResource(id = R.drawable.bitcoin_logo),
                contentDescription = "app icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(size = 16.dp))
                    .align(Alignment.CenterHorizontally)

            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = GeckoinTheme.typography.titleMedium,
                color = GeckoinTheme.customColors.textPrimary,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = modifier.size(110.dp))
            SettingsOptions(
                modifier = Modifier
                    .padding(top = 132.dp)
            )
        }
    }
}

@Composable
fun SettingsOptions(
    modifier: Modifier = Modifier
) {
    Column {
        SettingOptionItem(
            title = "Theme Mode",
            iconResId = if (isSystemInDarkTheme()) R.drawable.ic_moon else R.drawable.ic_sun
        ) {
            // TODO: open theme settings dialog
        }
        Divider(
            thickness = 1.dp,
            color = GeckoinTheme.customColors.dividerColor,
            startIndent = 50.dp
        )
        SettingOptionItem(title = "About Developer", iconResId = R.drawable.ic_code) {
            // TODO: open developer LinkedIn
//            openLinkInBrowser()
        }
        Divider(
            thickness = 1.dp,
            color = GeckoinTheme.customColors.dividerColor,
            startIndent = 50.dp
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
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable { action.invoke() }
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "option icon",
            tint = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier
                .size(24.dp)
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