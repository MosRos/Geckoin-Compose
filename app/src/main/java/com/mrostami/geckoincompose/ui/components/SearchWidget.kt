package com.mrostami.geckoincompose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchWidget(
    modifier: Modifier = Modifier,
    value: String = "search any coin",
    onQueryChanged: (String) -> Unit = {}
) {
    Box(modifier = modifier
        .background(
            color = GeckoinTheme.colorScheme.background,
            shape = GeckoinTheme.shapes.medium
        )
        .border(
            width = 1.dp,
            color = GeckoinTheme.colorScheme.outline,
            shape = GeckoinTheme.shapes.medium
        )
        .fillMaxWidth()
    ) {
        TextField(
            value = value,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = GeckoinTheme.colorScheme.surface,
                    shape = GeckoinTheme.shapes.medium
                ),
            textStyle = GeckoinTheme.typography.bodyMedium,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "search",
                    tint = GeckoinTheme.customColors.iconColor,
                    modifier = Modifier.padding(8.dp)
                )
            },
            shape = GeckoinTheme.shapes.medium,
            singleLine = true,
            colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                disabledTextColor = GeckoinTheme.customColors.iconColor,
                focusedIndicatorColor = Color.Transparent,
                textColor = GeckoinTheme.colorScheme.onSurface,
                cursorColor = GeckoinTheme.colorScheme.onSurface,
                backgroundColor = Color.Transparent,

            )
        )
    }
}