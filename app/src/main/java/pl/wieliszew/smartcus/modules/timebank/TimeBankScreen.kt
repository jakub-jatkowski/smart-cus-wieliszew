package pl.wieliszew.smartcus.modules.timebank

import androidx.compose.foundation.background

import androidx.compose.foundation.border

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Handshake

import androidx.compose.material.icons.filled.Schedule

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import pl.wieliszew.smartcus.data.model.TimeBankEntry

import pl.wieliszew.smartcus.data.model.TimeBankType

import pl.wieliszew.smartcus.data.repository.SmartCusRepository

import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard

import pl.wieliszew.smartcus.ui.components.StatusBadge

import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

import pl.wieliszew.smartcus.ui.theme.WieliszewGold

@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun TimeBankScreen(

    repository: SmartCusRepository

) {

    val userProfile by repository.userProfile.collectAsState()

    val entries by repository.timeBankEntries.collectAsState()

    val isSenior = LocalSeniorMode.current

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Wszystkie, 1: Oferty, 2: Poszukiwane

    var showAddDialog by remember { mutableStateOf(false) }

    var selectedEntryForExchange by remember { mutableStateOf<TimeBankEntry?>(null) }

    var exchangeSuccessMsg by remember { mutableStateOf<String?>(null) }

    val filteredList = entries.filter {

        when (selectedTab) {

            1 -> it.type == TimeBankType.OFFERING

            2 -> it.type == TimeBankType.REQUESTING

            else -> true

        }

    }

    Scaffold(

        floatingActionButton = {

            ExtendedFloatingActionButton(

                onClick = { showAddDialog = true },

                icon = { Icon(Icons.Default.Add, contentDescription = null) },

                text = { Text("Dodaj ofertę / potrzebę", fontWeight = FontWeight.Bold) },

                containerColor = MaterialTheme.colorScheme.primary,

                contentColor = MaterialTheme.colorScheme.onPrimary,

                modifier = Modifier.padding(bottom = 12.dp)

            )

        }

    ) { paddingValues ->

        LazyColumn(

            modifier = Modifier

                .fillMaxSize()

                .background(MaterialTheme.colorScheme.background)

                .padding(paddingValues)

                .padding(horizontal = 16.dp),

            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),

            verticalArrangement = Arrangement.spacedBy(14.dp)

        ) {

            // Time Wallet Header Card

            item {

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .clip(RoundedCornerShape(18.dp))

                        .background(if (isSenior) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.primaryContainer)

                        .then(

                            if (isSenior) Modifier.border(3.dp, WieliszewGold, RoundedCornerShape(18.dp)) else Modifier

                        )

                        .padding(18.dp)

                ) {

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Column {

                            Text(

                                text = "TWÓJ PORTFEL CZASU",

                                fontWeight = FontWeight.Bold,

                                fontSize = 11.sp,

                                color = if (isSenior) WieliszewGold else MaterialTheme.colorScheme.primary,

                                letterSpacing = 1.sp

                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(

                                text = "${userProfile.timeBankBalanceHours} godz.",

                                fontWeight = FontWeight.ExtraBold,

                                fontSize = if (isSenior) 30.sp else 26.sp,

                                color = MaterialTheme.colorScheme.onSurface

                            )

                            Text(

                                text = "Zasada: 1 godzina pomocy oddanej = 1 godzina odebranej",

                                fontSize = 11.sp,

                                color = MaterialTheme.colorScheme.onSurfaceVariant

                            )

                        }

                        Box(

                            modifier = Modifier

                                .size(52.dp)

                                .clip(CircleShape)

                                .background(MaterialTheme.colorScheme.primary),

                            contentAlignment = Alignment.Center

                        ) {

                            Icon(

                                imageVector = Icons.Default.Schedule,

                                contentDescription = null,

                                tint = Color.White,

                                modifier = Modifier.size(28.dp)

                            )

                        }

                    }

                }

            }

            // Tab Selector (Oferty / Poszukiwania)

            item {

                TabRow(

                    selectedTabIndex = selectedTab,

                    containerColor = MaterialTheme.colorScheme.surface

                ) {

                    Tab(

                        selected = selectedTab == 0,

                        onClick = { selectedTab = 0 },

                        text = { Text("Wszystko (${entries.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }

                    )

                    Tab(

                        selected = selectedTab == 1,

                        onClick = { selectedTab = 1 },

                        text = { Text("Oferują Pomoc", fontWeight = FontWeight.Bold, fontSize = 12.sp) }

                    )

                    Tab(

                        selected = selectedTab == 2,

                        onClick = { selectedTab = 2 },

                        text = { Text("Poszukują Pomocy", fontWeight = FontWeight.Bold, fontSize = 12.sp) }

                    )

                }

            }

            // List of Skills

            items(filteredList) { entry ->

                TimeBankEntryCard(

                    entry = entry,

                    isSenior = isSenior,

                    onExchange = { selectedEntryForExchange = entry }

                )

            }

        }

    }

    // Modal to add skill / offer

    if (showAddDialog) {

        var title by remember { mutableStateOf("") }

        var category by remember { mutableStateOf("Złota rączka") }

        var isOffer by remember { mutableStateOf(true) }

        var desc by remember { mutableStateOf("") }

        var hours by remember { mutableDoubleStateOf(1.0) }

        val categories = listOf("Złota rączka", "Edukacja cyfrowa", "Ogród", "Języki obce", "Gotowanie", "Transport")

        AlertDialog(

            onDismissRequest = { showAddDialog = false },

            title = { Text("Nowy wpis w Banku Czasu", fontWeight = FontWeight.Bold) },

            text = {

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        FilterChip(

                            selected = isOffer,

                            onClick = { isOffer = true },

                            label = { Text("Oferuję pomoc") }

                        )

                        FilterChip(

                            selected = !isOffer,

                            onClick = { isOffer = false },

                            label = { Text("Potrzebuję pomocy") }

                        )

                    }

                    OutlinedTextField(

                        value = title,

                        onValueChange = { title = it },

                        label = { Text("Tytuł umiejętności") },

                        placeholder = { Text("np. Naprawa kranu, nauka smartfona") },

                        modifier = Modifier.fillMaxWidth()

                    )

                    OutlinedTextField(

                        value = desc,

                        onValueChange = { desc = it },

                        label = { Text("Szczegóły i dogodne godziny") },

                        modifier = Modifier.fillMaxWidth(),

                        minLines = 2

                    )

                    Text("Kategoria:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

                        items(categories) { cat ->

                            FilterChip(

                                selected = category == cat,

                                onClick = { category = cat },

                                label = { Text(cat, fontSize = 11.sp) }

                            )

                        }

                    }

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Text("Szacowany czas:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        Row {

                            listOf(0.5, 1.0, 1.5, 2.0).forEach { h ->

                                FilterChip(

                                    selected = hours == h,

                                    onClick = { hours = h },

                                    label = { Text("${h}h", fontSize = 11.sp) },

                                    modifier = Modifier.padding(horizontal = 2.dp)

                                )

                            }

                        }

                    }

                }

            },

            confirmButton = {

                Button(

                    onClick = {

                        if (title.isNotBlank()) {

                            repository.addTimeBankEntry(

                                title = (if (isOffer) "Oferuję: " else "Poszukuję: ") + title,

                                village = userProfile.village,

                                type = if (isOffer) TimeBankType.OFFERING else TimeBankType.REQUESTING,

                                hours = hours,

                                category = category,

                                desc = desc

                            )

                            showAddDialog = false

                        }

                    }

                ) {

                    Text("Opublikuj", fontWeight = FontWeight.Bold)

                }

            },

            dismissButton = {

                TextButton(onClick = { showAddDialog = false }) {

                    Text("Anuluj")

                }

            }

        )

    }

    // Modal Confirmation for Exchange

    if (selectedEntryForExchange != null) {

        val entry = selectedEntryForExchange!!

        val isOffering = entry.type == TimeBankType.OFFERING

        AlertDialog(

            onDismissRequest = { selectedEntryForExchange = null },

            title = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(Icons.Default.Handshake, contentDescription = null, tint = MaterialTheme.colorScheme.primary)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Potwierdzenie wymiany czasowej", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                }

            },

            text = {

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text(

                        text = entry.title,

                        fontWeight = FontWeight.Bold,

                        style = MaterialTheme.typography.titleMedium

                    )

                    Text("Mieszkaniec: ${entry.authorName} (${entry.village})", fontSize = 13.sp)

                    Text(

                        text = if (isOffering) {

                            "Pobierzesz ${entry.hours}h ze swojego salda. Mieszkaniec otrzyma powiadomienie o nawiązaniu kontaktu sąsiedzkiego."

                        } else {

                            "Zarobisz ${entry.hours}h na swoje konto po udzieleniu wsparcia sąsiadowi."

                        },

                        style = MaterialTheme.typography.bodyMedium,

                        color = MaterialTheme.colorScheme.onSurfaceVariant

                    )

                }

            },

            confirmButton = {

                Button(

                    onClick = {

                        val delta = if (isOffering) -entry.hours else entry.hours

                        repository.transferTimeBankHours(entry.id, delta)

                        exchangeSuccessMsg = "Pomyślnie zrealizowano wymianę z: ${entry.authorName}!"

                        selectedEntryForExchange = null

                    }

                ) {

                    Text("Potwierdzam wymianę", fontWeight = FontWeight.Bold)

                }

            },

            dismissButton = {

                TextButton(onClick = { selectedEntryForExchange = null }) {

                    Text("Wróć")

                }

            }

        )

    }

    // Success Notification Dialog

    if (exchangeSuccessMsg != null) {

        AlertDialog(

            onDismissRequest = { exchangeSuccessMsg = null },

            title = { Text("Transakcja zrealizowana! 🎉", fontWeight = FontWeight.Bold) },

            text = { Text(exchangeSuccessMsg!!) },

            confirmButton = {

                TextButton(onClick = { exchangeSuccessMsg = null }) {

                    Text("Super, dziękuję!", fontWeight = FontWeight.Bold)

                }

            }

        )

    }

}

@Composable

fun TimeBankEntryCard(

    entry: TimeBankEntry,

    isSenior: Boolean,

    onExchange: () -> Unit

) {

    val isOffering = entry.type == TimeBankType.OFFERING

    SeniorAccessibleCard {

        Column {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                StatusBadge(

                    text = if (isOffering) "OFERTA" else "POTRZEBA",

                    containerColor = if (isOffering) Color(0xFFE8F5E9) else Color(0xFFEDE7F6),

                    contentColor = if (isOffering) Color(0xFF2E7D32) else Color(0xFF4527A0)

                )

                Text(

                    text = "⏳ ${entry.hours} godz.",

                    fontWeight = FontWeight.ExtraBold,

                    fontSize = 14.sp,

                    color = MaterialTheme.colorScheme.primary

                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = entry.title,

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold

            )

            Text(

                text = "Mieszkaniec: ${entry.authorName} (${entry.village}) • Kategoria: ${entry.category} • ⭐ ${entry.rating}",

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.onSurfaceVariant

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(

                text = entry.description,

                style = MaterialTheme.typography.bodyMedium

            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(

                onClick = onExchange,

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(

                    containerColor = if (isOffering) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

                )

            ) {

                Icon(Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(18.dp))

                Spacer(modifier = Modifier.width(6.dp))

                Text(

                    text = if (isOffering) "Wymień pomoc (${entry.hours}h)" else "Zaoferuj pomoc (+${entry.hours}h)",

                    fontWeight = FontWeight.Bold,

                    maxLines = 1,

                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

                )

            }

        }

    }

}

