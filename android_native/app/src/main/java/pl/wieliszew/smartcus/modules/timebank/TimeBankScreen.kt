package pl.wieliszew.smartcus.modules.timebank

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeBankScreen(
    repository: SmartCusRepository
) {
    val userProfile by repository.userProfile.collectAsState()
    val entries by repository.timeBankEntries.collectAsState()
    val isSenior = LocalSeniorMode.current

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Wszystkie, 1: Oferty, 2: Poszukiwane
    var showDialog by remember { mutableStateOf(false) }

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
                onClick = { showDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Dodaj umiejętność", fontWeight = FontWeight.Bold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
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
                                fontSize = 12.sp,
                                color = if (isSenior) WieliszewGold else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${userProfile.timeBankBalanceHours} godz.",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = if (isSenior) 32.sp else 28.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "1 godzina pomocy oddanej = 1 godzina odebranej",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
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
                        text = { Text("Wszystko (${entries.size})", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Mieszkańcy Oferują", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Poszukiwana Pomoc", fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // List of Skills
            items(filteredList) { entry ->
                TimeBankEntryCard(
                    entry = entry,
                    isSenior = isSenior,
                    onExchange = { repository.transferTimeBankHours(entry.id, 1.0) }
                )
            }
        }
    }

    // Modal to add skill / offer
    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Złota rączka") }
        var isOffer by remember { mutableStateOf(true) }
        var desc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Dodaj ogłoszenie w Banku Czasu", fontWeight = FontWeight.Bold) },
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
                        placeholder = { Text("np. Naprawa cieknącego kranu, lekcja włoskiego") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Szczegóły i dostępność") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
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
                                hours = 1.0,
                                category = category,
                                desc = desc
                            )
                            showDialog = false
                        }
                    }
                ) {
                    Text("Opublikuj w Banku Czasu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Anuluj")
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
                    text = if (isOffering) "OFERTA UMIEJĘTNOŚCI" else "POSZUKIWANA POMOC",
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Mieszkaniec: ${entry.authorName} (${entry.village}) • Ocena: ⭐ ${entry.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                    text = if (isOffering) "Wymień 1h ze swojego portfela" else "Zaoferuj pomoc (Zarób 1h)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

