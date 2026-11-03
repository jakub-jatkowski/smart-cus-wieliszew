package pl.wieliszew.smartcus.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Material Design 3 (Material You / M3) Color Tokens
 * Gmina Wieliszew Official Municipal Theme (Forest Green & Sage Tonal Palettes)
 * Conforming 100% to Google Material 3 Specifications
 */

// Light Tonal Palette
val M3PrimaryLight = Color(0xFF006C47)
val M3OnPrimaryLight = Color(0xFFFFFFFF)
val M3PrimaryContainerLight = Color(0xFF8EF7BE)
val M3OnPrimaryContainerLight = Color(0xFF002113)
val M3InversePrimaryLight = Color(0xFF71DAA3)

val M3SecondaryLight = Color(0xFF4D6353)
val M3OnSecondaryLight = Color(0xFFFFFFFF)
val M3SecondaryContainerLight = Color(0xFFCFE8D4)
val M3OnSecondaryContainerLight = Color(0xFF0B1F13)

val M3TertiaryLight = Color(0xFF3D6473)
val M3OnTertiaryLight = Color(0xFFFFFFFF)
val M3TertiaryContainerLight = Color(0xFFC0E9FB)
val M3OnTertiaryContainerLight = Color(0xFF001F29)

val M3ErrorLight = Color(0xFFBA1A1A)
val M3OnErrorLight = Color(0xFFFFFFFF)
val M3ErrorContainerLight = Color(0xFFFFDAD6)
val M3OnErrorContainerLight = Color(0xFF410002)

val M3BackgroundLight = Color(0xFFF7FAF5)
val M3OnBackgroundLight = Color(0xFF191C19)
val M3SurfaceLight = Color(0xFFF7FAF5)
val M3OnSurfaceLight = Color(0xFF191C19)
val M3SurfaceVariantLight = Color(0xFFDCE5DC)
val M3OnSurfaceVariantLight = Color(0xFF414942)
val M3OutlineLight = Color(0xFF717971)
val M3OutlineVariantLight = Color(0xFFC0C9C0)

// M3 Surface Container Roles (Tonal Elevation Tinting)
val M3SurfaceDimLight = Color(0xFFD8DCD6)
val M3SurfaceBrightLight = Color(0xFFF7FAF5)
val M3SurfaceContainerLowestLight = Color(0xFFFFFFFF)
val M3SurfaceContainerLowLight = Color(0xFFF1F5EF)
val M3SurfaceContainerLight = Color(0xFFEBF0EA)
val M3SurfaceContainerHighLight = Color(0xFFE5EAE4)
val M3SurfaceContainerHighestLight = Color(0xFFDFE4DE)

// Dark Tonal Palette
val M3PrimaryDark = Color(0xFF71DAA3)
val M3OnPrimaryDark = Color(0xFF003822)
val M3PrimaryContainerDark = Color(0xFF005334)
val M3OnPrimaryContainerDark = Color(0xFF8EF7BE)
val M3InversePrimaryDark = Color(0xFF006C47)

val M3SecondaryDark = Color(0xFFB4CCB8)
val M3OnSecondaryDark = Color(0xFF203527)
val M3SecondaryContainerDark = Color(0xFF364B3C)
val M3OnSecondaryContainerDark = Color(0xFFCFE8D4)

val M3TertiaryDark = Color(0xFFA5CDDE)
val M3OnTertiaryDark = Color(0xFF063544)
val M3TertiaryContainerDark = Color(0xFF234C5B)
val M3OnTertiaryContainerDark = Color(0xFFC0E9FB)

val M3ErrorDark = Color(0xFFFFB4AB)
val M3OnErrorDark = Color(0xFF690005)
val M3ErrorContainerDark = Color(0xFF93000A)
val M3OnErrorContainerDark = Color(0xFFFFDAD6)

val M3BackgroundDark = Color(0xFF111411)
val M3OnBackgroundDark = Color(0xFFE1E4DF)
val M3SurfaceDark = Color(0xFF111411)
val M3OnSurfaceDark = Color(0xFFE1E4DF)
val M3SurfaceVariantDark = Color(0xFF414942)
val M3OnSurfaceVariantDark = Color(0xFFC0C9C0)
val M3OutlineDark = Color(0xFF8B938B)
val M3OutlineVariantDark = Color(0xFF414942)

val M3SurfaceDimDark = Color(0xFF111411)
val M3SurfaceBrightDark = Color(0xFF373A36)
val M3SurfaceContainerLowestDark = Color(0xFF0C0F0C)
val M3SurfaceContainerLowDark = Color(0xFF191C19)
val M3SurfaceContainerDark = Color(0xFF1D201D)
val M3SurfaceContainerHighDark = Color(0xFF282B27)
val M3SurfaceContainerHighestDark = Color(0xFF323632)

// WCAG 2.1 AAA Senior / High Contrast Colors
val HighContrastBackground = Color(0xFF000000)
val HighContrastSurface = Color(0xFF0A0A0A)
val HighContrastContainer = Color(0xFF1A1A1A)
val HighContrastYellow = Color(0xFFFFD600)
val HighContrastWhite = Color(0xFFFFFFFF)
val HighContrastBorder = Color(0xFFFFD600)

// Backward compatibility references
val WieliszewGreenPrimary = M3PrimaryLight
val WieliszewGreenDark = Color(0xFF0C4A2D)
val WieliszewGreenLight = M3InversePrimaryLight
val WieliszewGreenContainer = M3PrimaryContainerLight
val WieliszewOnGreenContainer = M3OnPrimaryContainerLight

val WieliszewGold = Color(0xFFDDA72A)
val WieliszewGoldContainer = Color(0xFFFFF1D0)
val WieliszewOnGoldContainer = Color(0xFF4C3300)

val NeutralDark = M3OnSurfaceLight
val NeutralMedium = M3OnSurfaceVariantLight
val NeutralLight = M3BackgroundLight
val SurfaceCard = M3SurfaceContainerLowestLight
val OutlineBorder = M3OutlineVariantLight

// Solar Amber Palette
val SolarPrimary = Color(0xFFB26A00)
val SolarOnPrimary = Color(0xFFFFFFFF)
val SolarPrimaryContainer = Color(0xFFFFDDB8)
val SolarOnPrimaryContainer = Color(0xFF2B1700)
val SolarSecondary = Color(0xFF745B31)
val SolarSecondaryContainer = Color(0xFFF6EDE4)
val SolarSurface = Color(0xFFFFF8F4)

// Lake Azure Palette
val LakePrimary = Color(0xFF00668B)
val LakeOnPrimary = Color(0xFFFFFFFF)
val LakePrimaryContainer = Color(0xFFC3E8FF)
val LakeOnPrimaryContainer = Color(0xFF001E2D)
val LakeSecondary = Color(0xFF4E616D)
val LakeSecondaryContainer = Color(0xFFE7EFF6)
val LakeSurface = Color(0xFFF6FAFE)
