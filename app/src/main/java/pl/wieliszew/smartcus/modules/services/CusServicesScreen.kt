package pl.wieliszew.smartcus.modules.services

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
import pl.wieliszew.smartcus.modules.assistant.AiAssistantScreen
import pl.wieliszew.smartcus.modules.sharing.SharingHubScreen
import pl.wieliszew.smartcus.modules.telehealth.TelehealthScreen
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

/**
 * Zintegrowane Usługi Społeczne Centrum Usług Społecznych w Wieliszewie
 * Łączy dyżury specjalistów, magazyn współdzielenia / jadłodzielnię oraz punkt informacyjny
 */
@Composable
fun CusServicesScreen(
    repository: SmartCusRepository,
    initialTab: Int = 0,
    onNavigateToModule: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val isSenior = LocalSeniorMode.current

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.MedicalServices,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Dyżury",
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
                                imageVector = Icons.Outlined.VolunteerActivism,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sprzęt & Eko",
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
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.SupportAgent,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Doradca CUS",
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
            when (selectedTab) {
                0 -> TelehealthScreen(repository = repository)
                1 -> SharingHubScreen(repository = repository)
                else -> AiAssistantScreen(
                    repository = repository,
                    onNavigateToModule = onNavigateToModule
                )
            }
        }
    }
}

