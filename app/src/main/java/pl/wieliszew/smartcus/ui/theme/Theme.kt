package pl.wieliszew.smartcus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

val LocalSeniorMode = staticCompositionLocalOf { false }

/**
 * 100% Material Design 3 Light Color Scheme for Gmina Wieliszew
 */
val M3LightColorScheme = lightColorScheme(
    primary = M3PrimaryLight,
    onPrimary = M3OnPrimaryLight,
    primaryContainer = M3PrimaryContainerLight,
    onPrimaryContainer = M3OnPrimaryContainerLight,
    inversePrimary = M3InversePrimaryLight,

    secondary = M3SecondaryLight,
    onSecondary = M3OnSecondaryLight,
    secondaryContainer = M3SecondaryContainerLight,
    onSecondaryContainer = M3OnSecondaryContainerLight,

    tertiary = M3TertiaryLight,
    onTertiary = M3OnTertiaryLight,
    tertiaryContainer = M3TertiaryContainerLight,
    onTertiaryContainer = M3OnTertiaryContainerLight,

    error = M3ErrorLight,
    onError = M3OnErrorLight,
    errorContainer = M3ErrorContainerLight,
    onErrorContainer = M3OnErrorContainerLight,

    background = M3BackgroundLight,
    onBackground = M3OnBackgroundLight,
    surface = M3SurfaceLight,
    onSurface = M3OnSurfaceLight,
    surfaceVariant = M3SurfaceVariantLight,
    onSurfaceVariant = M3OnSurfaceVariantLight,
    surfaceTint = M3PrimaryLight,

    outline = M3OutlineLight,
    outlineVariant = M3OutlineVariantLight
)

/**
 * 100% Material Design 3 Dark Color Scheme for Gmina Wieliszew
 */
val M3DarkColorScheme = darkColorScheme(
    primary = M3PrimaryDark,
    onPrimary = M3OnPrimaryDark,
    primaryContainer = M3PrimaryContainerDark,
    onPrimaryContainer = M3OnPrimaryContainerDark,
    inversePrimary = M3InversePrimaryDark,

    secondary = M3SecondaryDark,
    onSecondary = M3OnSecondaryDark,
    secondaryContainer = M3SecondaryContainerDark,
    onSecondaryContainer = M3OnSecondaryContainerDark,

    tertiary = M3TertiaryDark,
    onTertiary = M3OnTertiaryDark,
    tertiaryContainer = M3TertiaryContainerDark,
    onTertiaryContainer = M3OnTertiaryContainerDark,

    error = M3ErrorDark,
    onError = M3OnErrorDark,
    errorContainer = M3ErrorContainerDark,
    onErrorContainer = M3OnErrorContainerDark,

    background = M3BackgroundDark,
    onBackground = M3OnBackgroundDark,
    surface = M3SurfaceDark,
    onSurface = M3OnSurfaceDark,
    surfaceVariant = M3SurfaceVariantDark,
    onSurfaceVariant = M3OnSurfaceVariantDark,
    surfaceTint = M3PrimaryDark,

    outline = M3OutlineDark,
    outlineVariant = M3OutlineVariantDark
)

/**
 * High Contrast WCAG 2.1 AAA Accessibility Scheme for Seniors
 */
val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastYellow,
    onPrimary = HighContrastBackground,
    primaryContainer = HighContrastContainer,
    onPrimaryContainer = HighContrastYellow,
    secondary = HighContrastYellow,
    onSecondary = HighContrastBackground,
    secondaryContainer = HighContrastContainer,
    onSecondaryContainer = HighContrastYellow,
    background = HighContrastBackground,
    onBackground = HighContrastWhite,
    surface = HighContrastSurface,
    onSurface = HighContrastWhite,
    outline = HighContrastBorder,
    outlineVariant = HighContrastBorder
)

@Composable
fun SmartCusTheme(
    isSeniorMode: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isSeniorMode -> HighContrastColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> M3DarkColorScheme
        else -> M3LightColorScheme
    }

    val typography = if (isSeniorMode) SeniorTypography else StandardTypography

    CompositionLocalProvider(LocalSeniorMode provides isSeniorMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = M3Shapes,
            content = content
        )
    }
}
