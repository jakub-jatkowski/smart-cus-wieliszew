package pl.wieliszew.smartcus.modules.sharing

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.LazyRow

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

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import pl.wieliszew.smartcus.data.model.ShareCategory

import pl.wieliszew.smartcus.data.model.SharedItem

import pl.wieliszew.smartcus.data.repository.SmartCusRepository

import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard

import pl.wieliszew.smartcus.ui.components.StatusBadge

import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

@Composable

fun SharingHubScreen(

    repository: SmartCusRepository

) {

    val items by repository.sharedItems.collectAsState()

    val isSenior = LocalSeniorMode.current

    var selectedCategory by remember { mutableStateOf<ShareCategory?>(null) }

    var showDonateDialog by remember { mutableStateOf(false) }

    var showPickupCodeDialog by remember { mutableStateOf<SharedItem?>(null) }

    val filteredItems = items.filter {

        selectedCategory == null || it.category == selectedCategory

    }

    LazyColumn(

        modifier = Modifier

            .fillMaxSize()

            .background(MaterialTheme.colorScheme.background)

            .padding(horizontal = 16.dp),

        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        // Hub Header Card

        item {

            SeniorAccessibleCard {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(

                        modifier = Modifier

                            .size(48.dp)

                            .clip(CircleShape)

                            .background(MaterialTheme.colorScheme.primaryContainer),

                        contentAlignment = Alignment.Center

                    ) {

                        Text("♻️", fontSize = 24.sp)

                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(

                            text = "Magazyn Współdzielenia & Eko",

                            style = MaterialTheme.typography.titleLarge,

                            fontWeight = FontWeight.Bold

                        )

                        Text(

                            text = "Cyrkularna wymiana dóbr i bezpłatne wypożyczalnie CUS Wieliszew",

                            style = MaterialTheme.typography.bodyMedium,

                            color = MaterialTheme.colorScheme.onSurfaceVariant

                        )

                    }

                }

            }

        }

        // Section: Gminna Jadłodzielnia Wieliszew (Featured Municipal Service)

        item {

            Card(

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)

            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text("🥗", fontSize = 24.sp)

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {

                                Text(

                                    text = "Gminna Jadłodzielnia Wieliszew",

                                    fontWeight = FontWeight.Bold,

                                    style = MaterialTheme.typography.titleMedium

                                )

                                Text(

                                    text = "ul. Modlińska 65 (obok CUS) • Czynne 24/7",

                                    fontSize = 12.sp,

                                    color = MaterialTheme.colorScheme.onSurfaceVariant

                                )

                            }

                        }

                        StatusBadge(

                            text = "OTWARTE 24/7",

                            containerColor = Color(0xFF2E7D32),

                            contentColor = Color.White

                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(

                        modifier = Modifier

                            .fillMaxWidth()

                            .clip(RoundedCornerShape(12.dp))

                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))

                            .padding(10.dp)

                    ) {

                        Text(

                            text = "Dziś w lodówce gminnej: świeże pieczywo rzemieślnicze, domowe przetwory warzywne, jabłka z sadu w Kałuszynie. Każdy może bezpłatnie skorzystać lub zostawić jedzenie!",

                            fontSize = 12.sp,

                            color = MaterialTheme.colorScheme.onPrimaryContainer,

                            lineHeight = 18.sp

                        )

                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    FilledTonalButton(

                        onClick = { showDonateDialog = true },

                        modifier = Modifier.fillMaxWidth()

                    ) {

                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("Przekaż dar do Jadłodzielni lub Magazynu", fontWeight = FontWeight.Bold)

                    }

                }

            }

        }

        // Category Filter Chips

        item {

            Column {

                Text(

                    text = "Dostępny sprzęt i zasoby:",

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.Bold,

                    color = MaterialTheme.colorScheme.primary

                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(

                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {

                    item {

                        FilterChip(

                            selected = selectedCategory == null,

                            onClick = { selectedCategory = null },

                            label = { Text("Wszystkie") }

                        )

                    }

                    items(ShareCategory.entries.toTypedArray()) { cat ->

                        FilterChip(

                            selected = selectedCategory == cat,

                            onClick = { selectedCategory = if (selectedCategory == cat) null else cat },

                            label = { Text(cat.label) }

                        )

                    }

                }

            }

        }

        // List of Shared Items

        items(filteredItems) { item ->

            SharedItemCard(

                item = item,

                isSenior = isSenior,

                onReserve = {

                    repository.reserveSharedItem(item.id)

                    if (!item.isReserved) {

                        showPickupCodeDialog = item.copy(isReserved = true, reservationCode = "CUS-W-2026-X8")

                    }

                }

            )

        }

    }

    // Modal: Zgłoś przekazanie daru

    if (showDonateDialog) {

        var donateTitle by remember { mutableStateOf("") }

        var donateDesc by remember { mutableStateOf("") }

        AlertDialog(

            onDismissRequest = { showDonateDialog = false },

            title = {

                Text("Przekaż dar dla mieszkańców", fontWeight = FontWeight.Bold)

            },

            text = {

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    Text(

                        text = "Możesz podzielić się nadwyżką świeżej żywności w Jadłodzielni (ul. Modlińska 65) lub sprzętem rehabilitacyjnym.",

                        fontSize = 12.sp,

                        color = MaterialTheme.colorScheme.onSurfaceVariant

                    )

                    OutlinedTextField(

                        value = donateTitle,

                        onValueChange = { donateTitle = it },

                        label = { Text("Co chcesz przekazać?") },

                        placeholder = { Text("np. 3 słoiki miodu, balkonik po rehabilitacji") },

                        modifier = Modifier.fillMaxWidth()

                    )

                    OutlinedTextField(

                        value = donateDesc,

                        onValueChange = { donateDesc = it },

                        label = { Text("Krótki opis i data ważności") },

                        modifier = Modifier.fillMaxWidth()

                    )

                }

            },

            confirmButton = {

                Button(

                    onClick = {

                        if (donateTitle.isNotBlank()) {

                            showDonateDialog = false

                        }

                    }

                ) {

                    Text("Zgłoś przekazanie", fontWeight = FontWeight.Bold)

                }

            },

            dismissButton = {

                TextButton(onClick = { showDonateDialog = false }) {

                    Text("Anuluj")

                }

            }

        )

    }

    // Modal: Wygenerowany kod odbioru

    if (showPickupCodeDialog != null) {

        val item = showPickupCodeDialog!!

        AlertDialog(

            onDismissRequest = { showPickupCodeDialog = null },

            title = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Sprzęt zarezerwowany!", fontWeight = FontWeight.Bold)

                }

            },

            text = {

                Column(

                    horizontalAlignment = Alignment.CenterHorizontally,

                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)

                ) {

                    Text(

                        text = item.title,

                        fontWeight = FontWeight.Bold,

                        style = MaterialTheme.typography.titleMedium,

                        color = MaterialTheme.colorScheme.onSurface

                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(

                        modifier = Modifier

                            .clip(RoundedCornerShape(12.dp))

                            .background(MaterialTheme.colorScheme.primaryContainer)

                            .padding(horizontal = 16.dp, vertical = 8.dp)

                    ) {

                        Text(

                            text = item.reservationCode ?: "CUS-W-2026-X8",

                            fontFamily = FontFamily.Monospace,

                            fontWeight = FontWeight.Black,

                            fontSize = 18.sp,

                            color = MaterialTheme.colorScheme.onPrimaryContainer

                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(

                        text = "Podaj powyższy kod pracownikowi CUS przy odbiorze przy ul. Modlińskiej 65 w godzinach 08:00 - 16:00.",

                        fontSize = 12.sp,

                        color = MaterialTheme.colorScheme.onSurfaceVariant

                    )

                }

            },

            confirmButton = {

                TextButton(onClick = { showPickupCodeDialog = null }) {

                    Text("Rozumiem, dziękuję!", fontWeight = FontWeight.Bold)

                }

            }

        )

    }

}

@Composable

fun SharedItemCard(

    item: SharedItem,

    isSenior: Boolean,

    onReserve: () -> Unit

) {

    SeniorAccessibleCard {

        Column {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                StatusBadge(

                    text = item.category.label,

                    containerColor = MaterialTheme.colorScheme.primaryContainer,

                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer

                )

                StatusBadge(

                    text = if (item.isReserved) "ZAREZERWOWANE" else "BEZPŁATNE",

                    containerColor = if (item.isReserved) Color(0xFFFFCC80) else Color(0xFFE8F5E9),

                    contentColor = if (item.isReserved) Color(0xFFE65100) else Color(0xFF2E7D32)

                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(

                text = item.title,

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold

            )

            Text(

                text = "📍 Odbiór: ${item.pickupLocation}",

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.primary,

                fontWeight = FontWeight.SemiBold

            )

            Text(

                text = "Stan: ${item.condition} • Dostępne sztuk: ${item.availableCount}",

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.onSurfaceVariant

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(

                text = item.description,

                style = MaterialTheme.typography.bodyMedium

            )

            if (item.isReserved && item.reservationCode != null) {

                Spacer(modifier = Modifier.height(8.dp))

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .clip(RoundedCornerShape(8.dp))

                        .background(Color(0xFFFFF3E0))

                        .padding(horizontal = 10.dp, vertical = 6.dp)

                ) {

                    Text(

                        text = "Twój kod odbioru: ${item.reservationCode}",

                        fontFamily = FontFamily.Monospace,

                        fontWeight = FontWeight.Bold,

                        color = Color(0xFFE65100),

                        fontSize = 12.sp

                    )

                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(

                onClick = onReserve,

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(

                    containerColor = if (item.isReserved) Color(0xFFC62828) else MaterialTheme.colorScheme.primary

                )

            ) {

                if (item.isReserved) {

                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))

                    Spacer(modifier = Modifier.width(6.dp))

                    Text("Anuluj rezerwację", fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)

                } else {

                    Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(18.dp))

                    Spacer(modifier = Modifier.width(6.dp))

                    Text("Zarezerwuj bezpłatnie", fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)

                }

            }

        }

    }

}

