package pl.wieliszew.smartcus.modules.volunteer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.modules.map.NeedMapScreen
import pl.wieliszew.smartcus.modules.timebank.TimeBankScreen
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

/**
 * Zintegrowany Moduł Pomocy Sąsiedzkiej i Wolontariatu CUS Wieliszew
 * Łączy Interaktywną Mapę OpenStreetMap oraz Bank Czasu w jednym przejrzystym widoku
 */
@Composable
fun VolunteerHubScreen(
    repository: SmartCusRepository,
    initialTab: Int = 0
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val isSenior = LocalSeniorMode.current

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            tonalElevation = 1.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Map,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mapa Pomocy",
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isSenior) 13.sp else 12.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Handshake,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bank Czasu",
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isSenior) 13.sp else 12.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (selectedTab == 0) {
                NeedMapScreen(repository = repository)
            } else {
                TimeBankScreen(repository = repository)
            }
        }
    }
}

