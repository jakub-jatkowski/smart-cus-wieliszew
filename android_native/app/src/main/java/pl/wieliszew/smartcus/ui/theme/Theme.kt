package pl.wieliszew.smartcus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSeniorMode = staticCompositionLocalOf { false }

private val StandardColorScheme = lightColorScheme(
    primary = WieliszewGreenPrimary,
    onPrimary = SurfaceCard,
    primaryContainer = WieliszewGreenContainer,
    onPrimaryContainer = WieliszewOnGreenContainer,
    secondary = WieliszewGold,
    onSecondary = NeutralDark,
    secondaryContainer = WieliszewGoldContainer,
    onSecondaryContainer = WieliszewOnGoldContainer,
    background = NeutralLight,
    surface = SurfaceCard,
    onSurface = NeutralDark,
    outline = OutlineBorder
)

private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastYellow,
    onPrimary = HighContrastBackground,
    primaryContainer = HighContrastSurface,
    onPrimaryContainer = HighContrastYellow,
    secondary = HighContrastYellow,
    onSecondary = HighContrastBackground,
    background = HighContrastBackground,
    surface = HighContrastSurface,
    onSurface = HighContrastWhite,
    outline = HighContrastBorder
)

@Composable
fun SmartCusTheme(
    isSeniorMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isSeniorMode) HighContrastColorScheme else StandardColorScheme
    val typography = if (isSeniorMode) SeniorTypography else StandardTypography

    CompositionLocalProvider(LocalSeniorMode provides isSeniorMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

