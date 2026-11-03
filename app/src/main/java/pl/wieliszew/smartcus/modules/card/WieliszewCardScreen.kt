package pl.wieliszew.smartcus.modules.card

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material.icons.outlined.*
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
val context = LocalContext.current
    val vouchers by repository.vouchers.collectAsState()
    val isSenior = LocalSeniorMode.current

    val cardProfiles = remember(userProfile, card) {
        listOf(
            Triple(userProfile.fullName, "Karta Główna", card.cardNumber),
            Triple("Stanisław Kowalski", "Karta Ucznia", "WKM-2026-88493"),
            Triple("Tadeusz Kowalski", "Karta Seniora", "WKM-2026-88494")
        )
    }
    var selectedProfileIndex by remember { mutableIntStateOf(0) }
    val activeProfile = cardProfiles[selectedProfileIndex]

    var isCardFlipped by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(42) }
    var showQrDialog by remember { mutableStateOf(false) }
    var showNfcDialog by remember { mutableStateOf(false) }

    // Dynamic Hologram Shimmer Animation
    val infiniteTransition = rememberInfiniteTransition(label = "cardEffects")
    val holoOffset by infiniteTransition.animateFloat(
        initialValue = -350f,
        targetValue = 950f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "holoOffset"
    )

    // NFC Radar Pulse Animation
    val nfcPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nfcPulse"
    )

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Wieliszewska Karta Mieszkańca (WKM)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Dotknij karty, aby obrócić na rewers z danymi ICE",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Wybór aktywnej karty w rodzinie (Karta Dużej Rodziny / Multi-Card)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(cardProfiles) { idx, prof ->
                        FilterChip(
                            selected = selectedProfileIndex == idx,
                            onClick = { selectedProfileIndex = idx },
                            label = {
                                Text(
                                    text = "${prof.first} • ${prof.second}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = when (idx) {
                                        1 -> Icons.Outlined.School
                                        2 -> Icons.Outlined.Elderly
                                        else -> Icons.Outlined.Badge
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

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
                        ) {
                            // Hologram Bezpieczeństwa (Security Shimmer Wave - Standard mObywatel 2.0)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0x15FFFFFF),
                                                Color(0x35E0F2FE),
                                                Color(0x45FEF08A),
                                                Color(0x35BBF7D0),
                                                Color(0x15FFFFFF),
                                                Color.Transparent
                                            ),
                                            start = Offset(holoOffset, 0f),
                                            end = Offset(holoOffset + 240f, 260f)
                                        )
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.SpaceBetween
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
                                            Icon(imageVector = Icons.Outlined.AccountBalance, contentDescription = null, tint = WieliszewGold, modifier = Modifier.size(22.dp))
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
                                        text = "POSIADACZ KARTY (${activeProfile.second.uppercase()})",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = activeProfile.first,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isSenior) 22.sp else 19.sp
                                    )
                                    Text(
                                        text = "ID: ${activeProfile.third}",
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
                                .graphicsLayer { this.rotationY = 180f }
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
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                userProfile.iceContact,
                                                color = Color(0xFFFF8A80),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFFD32F2F),
                                                modifier = Modifier.clickable {
                                                    try {
                                                        val intent = Intent(
                                                            Intent.ACTION_DIAL,
                                                            Uri.parse("tel:${userProfile.iceContact.replace(" ", "")}")
                                                        )
                                                        context.startActivity(intent)
                                                    } catch (e: Exception) {}
                                                }
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Phone,
                                                        contentDescription = "Zadzwoń ICE",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text("POŁĄCZ", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(38.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = { secondsLeft / 45f },
                                    modifier = Modifier.size(36.dp),
                                    strokeWidth = 3.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                                Text(
                                    text = "${secondsLeft}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
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
                        Icon(imageVector = Icons.Outlined.ConfirmationNumber, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Brak aktywnych voucherów",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Zbieraj punkty zaangażowania na Mapie Potrzeb i wymieniaj je na nagrody w zakładce 'Usługi CUS' pod zakładką Klub Mieszkańca.",
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

        // Sekcja: Oficjalne Uprawnienia i Ulgi Mieszkańca (WKM)
        item {
            Text(
                text = "Twoje Ulgi i Uprawnienia Mieszkańca",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(card.benefits) { benefit ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = benefit,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
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
                            .size(110.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outer Pulsing Ripple
                        Box(
                            modifier = Modifier
                                .size((80 * nfcPulse).dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                        )
                        // Inner Ring
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Nfc,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Gotowość do transmisji NFC",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Przytrzymaj tył telefonu przy czytniku bramki w Wieliszewie lub u kontrolera biletów.",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Aktywny profil: ${activeProfile.first}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp
                            )
                        }
                    }
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
