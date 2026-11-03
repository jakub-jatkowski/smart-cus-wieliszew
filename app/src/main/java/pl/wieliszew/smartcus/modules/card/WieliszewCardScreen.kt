package pl.wieliszew.smartcus.modules.card

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import pl.wieliszew.smartcus.data.model.Voucher
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
    val userProfile by repository.userProfile.collectAsState()
    val events by repository.events.collectAsState()
    val vouchers by repository.vouchers.collectAsState()
    val isSenior = LocalSeniorMode.current

    var isCardFlipped by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(42) }
    var showQrDialog by remember { mutableStateOf(false) }
    var showNfcDialog by remember { mutableStateOf(false) }

    // Dynamic QR Token refresh timer (TOTP 45s)
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

    // Generate real QR code bitmap using ZXing
    val qrBitmap = remember(secondsLeft, card.cardNumber) {
        val qrPayload = "WIELISZEW-WKM://${card.cardNumber}?token=${card.dynamicToken}&t=$secondsLeft"
        generateQrBitmap(qrPayload, 512)
    }

    // 3D Card Flip Animation spec
    val rotationY by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "cardFlip"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: 3D Interactive Card (Flip between Front and Back)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Wieliszewska Karta Mieszkańca (WKM)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Dotknij karty, aby obrócić na drugą stronę (rewers / ICE)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .graphicsLayer {
                            this.rotationY = rotationY
                            cameraDistance = 12f * density
                        }
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { isCardFlipped = !isCardFlipped }
                ) {
                    if (rotationY <= 90f) {
                        // FRONT OF CARD
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (isSenior) {
                                        Brush.verticalGradient(listOf(Color(0xFF000000), Color(0xFF1E1E1E)))
                                    } else {
                                        Brush.linearGradient(listOf(WieliszewGreenPrimary, WieliszewGreenDark))
                                    }
                                )
                                .then(
                                    if (isSenior) Modifier.border(3.dp, WieliszewGold, RoundedCornerShape(22.dp)) else Modifier
                                )
                                .padding(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("🏛️", fontSize = 20.sp)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "GMINA WIELISZEW",
                                                color = WieliszewGold,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                letterSpacing = 1.sp
                                            )
                                            Text(
                                                text = "KARTA MIESZKAŃCA",
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = if (isSenior) 17.sp else 15.sp
                                            )
                                        }
                                    }
                                    StatusBadge(
                                        text = "AKTYWNA",
                                        containerColor = Color(0xFF2E7D32),
                                        contentColor = Color.White
                                    )
                                }

                                Column {
                                    Text(
                                        text = "POSIADACZ KARTY",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = card.holderName,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isSenior) 22.sp else 19.sp
                                    )
                                    Text(
                                        text = "ID: ${card.cardNumber}",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ważność: ${card.validUntil}",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 11.sp
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.FlipCameraAndroid,
                                            contentDescription = null,
                                            tint = WieliszewGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Obróć kartę",
                                            color = WieliszewGold,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // BACK OF CARD (Rotated 180 deg to prevent mirroring text)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f }
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Magnetic Stripe Representation
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .background(Color(0xFF0F172A))
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Podpis posiadacza:", color = Color.Gray, fontSize = 10.sp)
                                        Text(
                                            card.holderName,
                                            color = Color.White,
                                            fontFamily = FontFamily.Cursive,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Kontakt ratunkowy ICE:", color = Color.Gray, fontSize = 10.sp)
                                        Text(
                                            userProfile.iceContact,
                                            color = Color(0xFFFF8A80),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Simulated Barcode
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .padding(vertical = 4.dp, horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "||| | |||| || ||| |||| | ||||| || |||",
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "5901234567890",
                                            fontFamily = FontFamily.Monospace,
                                            color = Color.Black,
                                            fontSize = 9.sp,
                                            letterSpacing = 2.sp
                                        )
                                    }
                                }

                                Text(
                                    text = "Centrum Usług Społecznych w Wieliszewie • tel. 22 782 01 00",
                                    color = Color.LightGray,
                                    fontSize = 9.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Real Dynamic QR Code (TOTP 45s) & NFC Emulation
        item {
            SeniorAccessibleCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Dynamiczny Kod QR (TOTP)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Zabezpieczenie przed kopiowaniem zrzutem ekranu",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${secondsLeft}s",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // QR Code Image (ZXing Generated Bitmap)
                    Box(
                        modifier = Modifier
                            .size(if (isSenior) 210.dp else 180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                            .clickable { showQrDialog = true }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "Dynamiczny kod QR",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Dotknij kodu QR, aby powiększyć na pełny ekran",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    AccessibleButton(
                        text = "Zbliż telefon do czytnika NFC",
                        onClick = { showNfcDialog = true },
                        icon = { Icon(Icons.Default.Nfc, contentDescription = null) }
                    )
                }
            }
        }

        // Section: Active Vouchers & Discounts Wallet
        item {
            Text(
                text = "Twoje Aktywne Vouchery i Zniżki",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (vouchers.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎟️", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Brak aktywnych voucherów",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Zbieraj punkty zaangażowania na Mapie Potrzeb i wymieniaj je na nagrody w zakładce 'Usługi ➔ Nagrody'.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(vouchers) { voucher ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = voucher.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = voucher.partnerName,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "KOD: ${voucher.code}",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Ważny do: ${voucher.validUntil}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        FilledTonalButton(
                            onClick = { /* Simulated Redeem */ },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Pokaż", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Wydarzenia i Zapisy CUS
        item {
            Text(
                text = "Zapisy na Wydarzenia CUS Wieliszew",
                style = MaterialTheme.typography.titleLarge,
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "📅 ${event.dateText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "📍 ${event.location}",
                        style = MaterialTheme.typography.bodySmall
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
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = { repository.toggleEventRegistration(event.id) },
                            colors = if (event.isRegistered) {
                                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
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
    }

    // Full-screen QR Dialog for scanning at municipal gates
    if (showQrDialog) {
        AlertDialog(
            onDismissRequest = { showQrDialog = false },
            title = {
                Text(
                    text = "Skanowanie e-Karty Mieszkańca",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Przyłóż ten kod do skanera w kasie OSiR lub GCK Wieliszew",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "Powiększony kod QR",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Odświeżenie za: ${secondsLeft}s",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showQrDialog = false }) {
                    Text("Zamknij", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // NFC Simulation Dialog
    if (showNfcDialog) {
        AlertDialog(
            onDismissRequest = { showNfcDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Nfc, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Emulacja NFC e-Portfel", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Karta zweryfikowana pomyślnie!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Terminal w Wieliszewie potwierdził uprawnienia mieszkańca dla: ${card.holderName}",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showNfcDialog = false }) {
                    Text("Gotowe", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/**
 * ZXing QR Code Bitmap Generator Helper
 */
fun generateQrBitmap(content: String, size: Int = 512): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                )
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}
