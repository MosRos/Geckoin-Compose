package com.mrostami.geckoincompose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val light_primary = Color(0xFF7870F1)
val light_onPrimary = Color(0xFFFFFFFF)
val light_primaryContainer = Color(0xFFBBB7F3)
val light_onPrimaryContainer = Color(0xFF2B2957)
val light_secondary = Color(0xFF8B95EA)
val light_onSecondary = Color(0xFFFFFFFF)
val light_secondaryContainer = Color(0xFFB6BADC)
val light_onSecondaryContainer = Color(0xFF262A51)
val light_tertiary = Color(0xFFE8B827)
val light_onTertiary = Color(0xFFFFFFFF)
val light_tertiaryContainer = Color(0xFFF1E1AF)
val light_onTertiaryContainer = Color(0xFF473C1A)
val light_error = Color(0xFFBA1A1A)
val light_errorContainer = Color(0xFFFFDAD6)
val light_onError = Color(0xFFFFFFFF)
val light_onErrorContainer = Color(0xFF410002)
val light_background = Color(0xFFEFEFFB)
val light_onBackground = Color(0xFF1D1B1E)
val light_surface = Color(0xFFF4F4FA)
val light_onSurface = Color(0xFF252525)
val light_surfaceVariant = Color(0xFFE8E0EB)
val light_onSurfaceVariant = Color(0xFF4A454E)
val light_outline = Color(0xFFFFFFFF)
val light_inverseOnSurface = Color(0xFFF5EFF4)
val light_inverseSurface = Color(0xFF1D1B1E)
val light_inversePrimary = Color(0xFFD9B9FF)
val light_shadow = Color(0xFF000000)
val light_surfaceTint = Color(0xFF8307F0)

val light_divider = Color(0XFFE2E1EA)
val light_icon = Color(0XFF9593AA)
val light_text_primary = Color(0XFF1E1E1E)
val light_text_secondary = Color(0XFF363636)
val light_up_green = Color(0XFF03B94D)
val light_down_red = Color(0XFFD32F2F)


// dark color
val dark_primary = Color(0xFF8888FA)
val dark_onPrimary = Color(0xFFFFFFFF)
val dark_primaryContainer = Color(0xFFC8C8FA)
val dark_onPrimaryContainer = Color(0xFF2F2F5E)
val dark_secondary = Color(0xFF8390FA)
val dark_onSecondary = Color(0xFFFFFFFF)
val dark_secondaryContainer = Color(0xFFC3C9FA)
val dark_onSecondaryContainer = Color(0xFF2E335E)
val dark_tertiary = Color(0xFFE8B827)
val dark_onTertiary = Color(0xFFFFFFFF)
val dark_tertiaryContainer = Color(0xFFF1E1AF)
val dark_onTertiaryContainer = Color(0xFF473C1A)
val dark_error = Color(0xFFBA1A1A)
val dark_errorContainer = Color(0xFFFFDAD6)
val dark_onError = Color(0xFFFFFFFF)
val dark_onErrorContainer = Color(0xFF410002)
val dark_background = Color(0xFF1E2B38)
val dark_onBackground = Color(0xFFFFFFFF)
val dark_surface = Color(0xFF293849)
val dark_onSurface = Color(0xFFD6D6D6)
val dark_surfaceVariant = Color(0xFF273849)
val dark_onSurfaceVariant = Color(0xFFFFFFFF)
val dark_outline = Color(0xFF314255)
val dark_inverseOnSurface = Color(0xFF363436)
val dark_inverseSurface = Color(0xFFEFEBF1)
val dark_inversePrimary = Color(0xFFE8DAF8)
val dark_shadow = Color(0xFFE5E0E0)
val dark_surfaceTint = Color(0xFF8307F0)

val dark_divider = Color(0XFF3F4E5E)
val dark_icon = Color(0XFF758697)
val dark_text_primary = Color(0XFFF8F8F8)
val dark_text_secondary = Color(0XFFC8C8C8)
val dark_up_green = Color(0XFF058C41)
val dark_down_red = Color(0XFFD33030)

@Immutable
data class CustomColors(
    val textPrimary: Color = light_text_primary,
    val textSecondary: Color = light_text_secondary,
    val dividerColor: Color = light_divider,
    val iconColor: Color = light_icon,
    val upGreen: Color = light_up_green,
    val downRed: Color = light_down_red
)

internal val LocalCustomColors = staticCompositionLocalOf { CustomColors() }
