package pl.wieliszew.smartcus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.model.MunicipalNotification
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.M3Shapes
import pl.wieliszew.smartcus.ui.theme.MunicipalTheme

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
    unreadNotificationsCount: Int = 0,
    onOpenNotifications: () -> Unit,
    onOpenThemeSelector: () -> Unit,
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
                .padding(horizontal = 16.dp, vertical = 10.dp),
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Theme Selector Button
                IconButton(onClick = onOpenThemeSelector) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Zmień motyw",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Notification Bell with Badge
                IconButton(onClick = onOpenNotifications) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationsCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ) {
                                    Text(text = unreadNotificationsCount.toString(), fontSize = 10.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Powiadomienia gminne",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // M3 Filled Tonal Button for Senior Toggle
                FilledTonalButton(
                    onClick = onToggleSeniorMode,
                    shape = RoundedCornerShape(9999.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isSeniorMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (isSeniorMode) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    border = if (isSeniorMode) BorderStroke(2.dp, MaterialTheme.colorScheme.outline) else null,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isSeniorMode) "Aa Normalny" else "Aa Senior AAA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Dialog Powiadomień Gminnych CUS Wieliszew
 */
@Composable
fun NotificationsDialog(
    notifications: List<MunicipalNotification>,
    onDismiss: () -> Unit
) {
    val isSenior = LocalSeniorMode.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Komunikaty Gminne CUS",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (notifications.isEmpty()) {
                    Text("Brak nowych powiadomień.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    notifications.forEach { notif ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (notif.isAlert) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                                else MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (notif.isAlert) {
                                        Text(
                                            text = "ALERT BEZPIECZEŃSTWA",
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    } else {
                                        Text(
                                            text = "KOMUNIKAT",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    }
                                    Text(
                                        text = notif.timeAgo,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isSenior) 16.sp else 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = notif.description,
                                    fontSize = if (isSenior) 14.sp else 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Zamknij", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Dialog Wyboru Motywu Material You
 */
@Composable
fun ThemeSelectorDialog(
    currentTheme: MunicipalTheme,
    onSelectTheme: (MunicipalTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Wybierz Motyw Aplikacji", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MunicipalTheme.entries.forEach { theme ->
                    val isSelected = theme == currentTheme
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectTheme(theme)
                                onDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
                        ),
                        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(theme.emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = theme.displayName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
