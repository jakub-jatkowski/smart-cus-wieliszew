package pl.wieliszew.smartcus.modules.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.model.NeedCategory
import pl.wieliszew.smartcus.data.model.NeedPin
import pl.wieliszew.smartcus.data.model.NeedStatus
import pl.wieliszew.smartcus.data.model.Urgency
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.AccessibleButton
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.components.StatusBadge
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeedMapScreen(
    repository: SmartCusRepository
) {
    val pins by repository.needPins.collectAsState()
    val isSenior = LocalSeniorMode.current

    var selectedVillage by remember { mutableStateOf("Wszystkie") }
    var showAddDialog by remember { mutableStateOf(false) }

    val villages = listOf("Wszystkie", "Wieliszew", "Skrzeszew", "Michałów-Reginów", "Komornica", "Kałuszyn", "Łajski")

    val filteredPins = pins.filter {
        selectedVillage == "Wszystkie" || it.village.equals(selectedVillage, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Zgłoś potrzebę", fontWeight = FontWeight.Bold) },
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
            // Header Info Card
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
                            Text("📍", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Interaktywna Mapa Potrzeb",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Zanonimizowane pinezki mikrowolontariatu w Gminie Wieliszew",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Simulated Interactive Map Radar View
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE2EFE9))
                        .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
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
                            StatusBadge(
                                text = "📡 Widok GPS: Sołectwa Wieliszewa",
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                            Text(
                                text = "Aktywne pinezki: ${filteredPins.count { it.status == NeedStatus.OPEN }}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp
                            )
                        }

                        // Simulated radar pins representation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            filteredPins.take(4).forEach { pin ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (pin.status == NeedStatus.IN_PROGRESS) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(pin.category.icon, fontSize = 20.sp)
                                    }
                                    Text(
                                        text = pin.village,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Text(
                            text = "ℹ Dokładny adres jest chroniony RODO do momentu podjęcia zgłoszenia.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Village Filter Chips
            item {
                Column {
                    Text(
                        text = "Filtruj sołectwo:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(villages) { v ->
                            FilterChip(
                                selected = selectedVillage == v,
                                onClick = { selectedVillage = v },
                                label = { Text(v, fontWeight = FontWeight.SemiBold) }
                            )
                        }
                    }
                }
            }

            // Need Pins List
            items(filteredPins) { pin ->
                NeedPinCard(
                    pin = pin,
                    isSenior = isSenior,
                    onAccept = { repository.acceptNeedPin(pin.id) }
                )
            }
        }
    }

    // Modal Dialog: Dodaj nową potrzebę
    if (showAddDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newDesc by remember { mutableStateOf("") }
        var newVillage by remember { mutableStateOf("Wieliszew") }
        var selectedCat by remember { mutableStateOf(NeedCategory.SHOPPING) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Zgłoś potrzebę pomocy sąsiedzkiej",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("W czym potrzebujesz pomocy?") },
                        placeholder = { Text("np. Zakupy w aptece, wniesienie paczki") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text("Krótki opis sytuacji") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Text("Twoje sołectwo: $newVillage", fontWeight = FontWeight.Medium)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            repository.addNeedPin(
                                title = newTitle,
                                category = selectedCat,
                                village = newVillage,
                                description = newDesc,
                                urgency = Urgency.TODAY
                            )
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Dodaj zgłoszenie")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Anuluj")
                }
            }
        )
    }
}

@Composable
fun NeedPinCard(
    pin: NeedPin,
    isSenior: Boolean,
    onAccept: () -> Unit
) {
    val isAccepted = pin.status == NeedStatus.IN_PROGRESS

    SeniorAccessibleCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(pin.category.icon, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    StatusBadge(
                        text = pin.category.label,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                StatusBadge(
                    text = if (isAccepted) "W TRAKCIE REALIZACJI" else pin.urgency.label,
                    containerColor = if (isAccepted) Color(0xFF2E7D32) else Color(0xFFFFE082),
                    contentColor = if (isAccepted) Color.White else Color(0xFF4E342E)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = pin.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "📍 ${pin.village} • ${pin.approximateAddress}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pin.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+${pin.pointsReward} pkt wolontariatu",
                    color = Color(0xFFF57F17),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                if (isAccepted) {
                    FilledTonalButton(
                        onClick = {},
                        enabled = false
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Podjęto pomoc", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Chcę pomóc", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

