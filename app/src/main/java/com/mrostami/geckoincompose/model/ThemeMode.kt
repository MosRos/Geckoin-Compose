package com.mrostami.geckoincompose.model

enum class ThemeMode(val id: Int) {
        AUTO(id = 0),
        LIGHT(id = 1),
        DARK(id = 2)
}

fun Int.toThemeMode() : ThemeMode {
    return when(this) {
        ThemeMode.AUTO.id -> ThemeMode.AUTO
        ThemeMode.LIGHT.id -> ThemeMode.LIGHT
        ThemeMode.DARK.id -> ThemeMode.DARK
        else -> ThemeMode.AUTO
    }
}

fun ThemeMode.inDarkMode(isSystemDark: Boolean) : Boolean {
    return this == ThemeMode.DARK || (this == ThemeMode.AUTO && isSystemDark)
}