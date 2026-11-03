package pl.wieliszew.smartcus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.model.MunicipalNotification
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.M3Shapes
import pl.wieliszew.smartcus.ui.theme.MunicipalTheme
import androidx.compose.material.icons.outlined.*
import pl.wieliszew.smartcus.data.model.NeedCategory
import pl.wieliszew.smartcus.data.model.ShareCategory
import pl.wieliszew.smartcus.data.model.SpecialistType

@Composable
fun NeedCategoryIcon(
    category: NeedCategory,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    val icon = when (category) {
        NeedCategory.SHOPPING -> Icons.Outlined.ShoppingCart
        NeedCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
        NeedCategory.REPAIR -> Icons.Outlined.Build
        NeedCategory.COMPANIONSHIP -> Icons.Outlined.Forum
        NeedCategory.PET_CARE -> Icons.Outlined.Pets
        NeedCategory.GARDEN -> Icons.Outlined.Yard
    }
    Icon(imageVector = icon, contentDescription = category.label, modifier = modifier, tint = tint)
}

@Composable
fun ShareCategoryIcon(
    category: ShareCategory,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    val icon = when (category) {
        ShareCategory.REHAB_EQUIPMENT -> Icons.Outlined.MedicalServices
        ShareCategory.FOOD_SHARING -> Icons.Outlined.Restaurant
        ShareCategory.CLOTHING_GOODS -> Icons.Outlined.Checkroom
        ShareCategory.TOOLS_EQUIPMENT -> Icons.Outlined.HomeRepairService
    }
    Icon(imageVector = icon, contentDescription = category.label, modifier = modifier, tint = tint)
}

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
    val minHeight = if (isSenior) 54.dp else 48.dp
    val shape = if (isSenior) RoundedCornerShape(14.dp) else RoundedCornerShape(9999.dp)

    if (isPrimary) {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight),
            shape = shape,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    style = if (isSenior) MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp) else MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    } else {
        FilledTonalButton(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight),
            shape = shape,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    style = if (isSenior) MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp) else MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
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
            .padding(horizontal = if (isSenior) 10.dp else 8.dp, vertical = if (isSenior) 5.dp else 3.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = if (isSenior) 13.sp else 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Material Design 3 Top App Bar / Header Banner
 * Clean, modern consumer app header with statusBarsPadding()
 */
@Composable
fun HeaderBanner(
    title: String,
    subtitle: String,
    isSeniorMode: Boolean,
    unreadNotificationsCount: Int = 0,
    isBackendOnline: Boolean = true,
    onOpenBackendSettings: (() -> Unit)? = null,
    onOpenNotifications: () -> Unit,
    onOpenThemeSelector: () -> Unit,
    onToggleSeniorMode: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Mark & App Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenBackendSettings?.invoke() }
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Spa,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Wieliszew",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 17.sp,
                                letterSpacing = (-0.2).sp
                            )
                            if (!isBackendOnline) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF59E0B))
                                )
                            }
                        }
                        Text(
                            text = "Centrum Usług Społecznych",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Sleek Action Icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Senior Accessibility Toggle
                    IconButton(
                        onClick = onToggleSeniorMode,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSeniorMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            contentColor = if (isSeniorMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Aa",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Theme Selector Button
                    IconButton(
                        onClick = onOpenThemeSelector,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Zmień motyw",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Notification Bell with Badge
                    IconButton(
                        onClick = onOpenNotifications,
                        modifier = Modifier.size(36.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationsCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    ) {
                                        Text(text = unreadNotificationsCount.toString(), fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Powiadomienia",
                                modifier = Modifier.size(21.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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

/**
 * Dialog Konfiguracji Serwera i Połączenia Backendowego CUS
 */
@Composable
fun BackendSettingsDialog(
    currentUrl: String,
    isOnline: Boolean,
    onSaveUrl: (String) -> Unit,
    onTestConnection: () -> Unit,
    onDismiss: () -> Unit
) {
    var inputUrl by remember { mutableStateOf(currentUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Dns,
                    contentDescription = null,
                    tint = if (isOnline) Color(0xFF22C55E) else Color(0xFFF59E0B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Serwer Backend CUS API", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOnline) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isOnline) Color(0xFF2E7D32) else Color(0xFFE65100))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOnline) "Połączono z serwerem CUS (Baza Live)" else "Tryb lokalny (Brak łączności z serwerem)",
                            color = if (isOnline) Color(0xFF1B5E20) else Color(0xFFBF360C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Text(
                    text = "Aplikacja synchronizuje dane z serwerem FastAPI / SQLite w Gminie Wieliszew. Możesz zmienić adres IP serwera:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = { inputUrl = it },
                    label = { Text("Adres URL serwera API") },
                    placeholder = { Text("http://10.0.2.2:8000") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = { inputUrl = "http://10.0.2.2:8000" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Emulator", fontSize = 11.sp)
                    }
                    FilledTonalButton(
                        onClick = { inputUrl = "http://localhost:8000" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Localhost", fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveUrl(inputUrl.trim())
                    onTestConnection()
                    onDismiss()
                }
            ) {
                Text("Zapisz i Połącz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
