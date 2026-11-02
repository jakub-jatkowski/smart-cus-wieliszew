package pl.wieliszew.smartcus.modules.card

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.AccessibleButton
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.components.StatusBadge
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.WieliszewGold
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenDark
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenPrimary

@Composable
fun WieliszewCardScreen(
    repository: SmartCusRepository
) {
    val card by repository.residentCard.collectAsState()
    val events by repository.events.collectAsState()
    val isSenior = LocalSeniorMode.current

    var isNfcSimulating by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(42) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            if (secondsLeft > 1) {
                secondsLeft -= 1
            } else {
                secondsLeft = 45
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // e-Card Wallet Graphic
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSenior) {
                            Brush.verticalGradient(listOf(Color(0xFF000000), Color(0xFF222222)))
                        } else {
                            Brush.linearGradient(listOf(WieliszewGreenPrimary, WieliszewGreenDark))
                        }
                    )
                    .then(
                        if (isSenior) Modifier.border(3.dp, WieliszewGold, RoundedCornerShape(20.dp)) else Modifier
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "GMINA WIELISZEW",
                                color = WieliszewGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "e-Karta Mieszkańca",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (isSenior) 24.sp else 20.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Nfc,
                            contentDescription = "NFC Gotowe",
                            tint = WieliszewGold,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = card.holderName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isSenior) 26.sp else 22.sp
                    )
                    Text(
                        text = "Numer: ${card.cardNumber}",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = if (isSenior) 16.sp else 14.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            text = "STATUS: AKTYWNA",
                            containerColor = Color(0xFF2E7D32),
                            contentColor = Color.White
                        )
                        Text(
                            text = "Ważna do: ${card.validUntil}",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Dynamic QR and NFC Actions
        item {
            SeniorAccessibleCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Twój Dynamiczny Kod QR (TOTP)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Zabezpieczenie przed kopiowaniem. Ważny jeszcze: ${secondsLeft}s",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated QR Code Container
                    Box(
                        modifier = Modifier
                            .size(if (isSenior) 220.dp else 190.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "Dynamiczny kod QR",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AccessibleButton(
                        text = if (isNfcSimulating) "Zbliżono telefon do czytnika NFC!" else "Zbliż telefon (NFC e-Portfel)",
                        onClick = { isNfcSimulating = !isNfcSimulating },
                        icon = { Icon(Icons.Default.Nfc, contentDescription = null) }
                    )
                }
            }
        }

        // Section: Wydarzenia i Rejestracja CUS
        item {
            Text(
                text = "Zapisy na Wydarzenia CUS Wieliszew",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(events) { event ->
            SeniorAccessibleCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            text = event.category,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (event.isRegistered) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ZAPISANY",
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "📅 ${event.dateText}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "📍 ${event.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Miejsca: ${event.spotsTaken} / ${event.spotsTotal}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = { repository.toggleEventRegistration(event.id) },
                            colors = if (event.isRegistered) {
                                ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                            } else {
                                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            }
                        ) {
                            Text(
                                text = if (event.isRegistered) "Zrezygnuj" else "Zapisz się z e-Kartą",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section: Uprawnienia i Zniżki
        item {
            SeniorAccessibleCard {
                Text(
                    text = "Twoje Ulgi i Korzyści z e-Kartą",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                card.benefits.forEach { b ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "✔ ", color = WieliszewGreenPrimary, fontWeight = FontWeight.Bold)
                        Text(text = b, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

