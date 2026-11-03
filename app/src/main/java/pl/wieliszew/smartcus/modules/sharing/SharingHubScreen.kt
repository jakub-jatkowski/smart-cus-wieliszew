package pl.wieliszew.smartcus.modules.sharing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.model.ShareCategory
import pl.wieliszew.smartcus.data.model.SharedItem
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.components.StatusBadge
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenPrimary

@Composable
fun SharingHubScreen(
    repository: SmartCusRepository
) {
    val items by repository.sharedItems.collectAsState()
    val isSenior = LocalSeniorMode.current

    var selectedCategory by remember { mutableStateOf<ShareCategory?>(null) }

    val filteredItems = items.filter {
        selectedCategory == null || it.category == selectedCategory
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
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
                            text = "Magazyn Współdzielenia i Jadłodzielnia",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cyrkularna wymiana dóbr i bezpłatne wypożyczalnie CUS Wieliszew",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            Column {
                Text(
                    text = "Kategorie zasobów:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
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
                            onClick = { selectedCategory = cat },
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
                onReserve = { repository.reserveSharedItem(item.id) }
            )
        }
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "📍 Odbiór: ${item.pickupLocation}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Stan: ${item.condition} • Dostępne sztuk: ${item.availableCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onReserve,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.isReserved) Color(0xFFC62828) else MaterialTheme.colorScheme.primary
                )
            ) {
                if (item.isReserved) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Zarezerwowano (Kod odbioru wygenerowany)", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Zarezerwuj do bezpłatnego odbioru", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

