package pl.wieliszew.smartcus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.M3Shapes

/**
 * Material Design 3 Accessible Card
 * Conforms to M3 Card & Outlined Card specifications
 */
@Composable
fun SeniorAccessibleCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isSenior = LocalSeniorMode.current
    val border = if (isSenior) {
        BorderStroke(3.dp, MaterialTheme.colorScheme.outline)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    }
    val shape = if (isSenior) RoundedCornerShape(12.dp) else M3Shapes.large

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSenior) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSenior) 0.dp else 1.dp,
            hoveredElevation = 2.dp,
            pressedElevation = 2.dp
        ),
        border = border
    ) {
        Column(
            modifier = Modifier.padding(if (isSenior) 20.dp else 16.dp)
        ) {
            content()
        }
    }
}

/**
 * Material Design 3 Button (Filled / Tonal / Outlined)
 */
@Composable
fun AccessibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    val isSenior = LocalSeniorMode.current
    val height = if (isSenior) 56.dp else 48.dp
    val shape = if (isSenior) RoundedCornerShape(12.dp) else RoundedCornerShape(9999.dp)

    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 1.dp),
            border = if (isSenior) BorderStroke(3.dp, MaterialTheme.colorScheme.outline) else null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = if (isSenior) MaterialTheme.typography.titleLarge else MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        FilledTonalButton(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            shape = shape,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            elevation = ButtonDefaults.filledTonalButtonElevation(defaultElevation = 0.dp, pressedElevation = 1.dp),
            border = if (isSenior) BorderStroke(3.dp, MaterialTheme.colorScheme.outline) else null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = if (isSenior) MaterialTheme.typography.titleLarge else MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Material Design 3 Chip / Badge
 */
@Composable
fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val isSenior = LocalSeniorMode.current
    Box(
        modifier = modifier
            .clip(M3Shapes.small)
            .background(containerColor)
            .padding(horizontal = if (isSenior) 12.dp else 10.dp, vertical = if (isSenior) 6.dp else 4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = if (isSenior) 14.sp else 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp
        )
    }
}

/**
 * Material Design 3 Top App Bar / Header Banner
 */
@Composable
fun HeaderBanner(
    title: String,
    subtitle: String,
    isSeniorMode: Boolean,
    onToggleSeniorMode: () -> Unit
) {
    Surface(
        color = if (isSeniorMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // M3 Filled Tonal Button for Senior Toggle
            FilledTonalButton(
                onClick = onToggleSeniorMode,
                shape = RoundedCornerShape(9999.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isSeniorMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (isSeniorMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = if (isSeniorMode) BorderStroke(2.dp, MaterialTheme.colorScheme.outline) else null
            ) {
                Text(
                    text = if (isSeniorMode) "Aa Normalny" else "Aa Tryb Seniora",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
