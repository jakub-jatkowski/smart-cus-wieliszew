package pl.wieliszew.smartcus

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.modules.assistant.AiAssistantScreen
import pl.wieliszew.smartcus.modules.card.WieliszewCardScreen
import pl.wieliszew.smartcus.modules.gamification.GamificationScreen
import pl.wieliszew.smartcus.modules.map.NeedMapScreen
import pl.wieliszew.smartcus.modules.sharing.SharingHubScreen
import pl.wieliszew.smartcus.modules.telehealth.TelehealthScreen
import pl.wieliszew.smartcus.modules.timebank.TimeBankScreen
import pl.wieliszew.smartcus.ui.components.HeaderBanner
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.SmartCusTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Assistant : Screen("assistant", "Asystent AI", Icons.Default.SmartToy)
    data object Card : Screen("card", "e-Karta", Icons.Default.CreditCard)
    data object Map : Screen("map", "Mapa Potrzeb", Icons.Default.Place)
    data object TimeBank : Screen("timebank", "Bank Czasu", Icons.Default.Schedule)
    data object Gamification : Screen("gamification", "Nagrody", Icons.Default.EmojiEvents)
    data object Telehealth : Screen("telehealth", "Teleopieka", Icons.Default.VideoCall)
    data object Sharing : Screen("sharing", "Współdzielenie", Icons.Default.Autorenew)
}

@Composable
fun SmartCusApp(repository: SmartCusRepository) {
    val isSeniorMode by repository.isSeniorMode.collectAsState()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Assistant) }

    val navItems = listOf(
        Screen.Assistant,
        Screen.Card,
        Screen.Map,
        Screen.TimeBank,
        Screen.Gamification,
        Screen.Telehealth,
        Screen.Sharing
    )

    SmartCusTheme(isSeniorMode = isSeniorMode) {
        Scaffold(
            topBar = {
                HeaderBanner(
                    title = "Smart CUS Wieliszew",
                    subtitle = "Cyfrowy Hub Usług Społecznych",
                    isSeniorMode = isSeniorMode,
                    onToggleSeniorMode = { repository.toggleSeniorMode() }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = if (isSeniorMode) 12.sp else 10.sp,
                                    maxLines = 1
                                )
                            },
                            selected = currentScreen.route == screen.route,
                            onClick = { currentScreen = screen }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    Screen.Assistant -> AiAssistantScreen(
                        repository = repository,
                        onNavigateToModule = { target ->
                            when (target) {
                                "card" -> currentScreen = Screen.Card
                                "map" -> currentScreen = Screen.Map
                                "timebank" -> currentScreen = Screen.TimeBank
                                "telehealth" -> currentScreen = Screen.Telehealth
                                "sharing" -> currentScreen = Screen.Sharing
                                "gamification" -> currentScreen = Screen.Gamification
                                "events" -> currentScreen = Screen.Card
                            }
                        }
                    )
                    Screen.Card -> WieliszewCardScreen(repository = repository)
                    Screen.Map -> NeedMapScreen(repository = repository)
                    Screen.TimeBank -> TimeBankScreen(repository = repository)
                    Screen.Gamification -> GamificationScreen(repository = repository)
                    Screen.Telehealth -> TelehealthScreen(repository = repository)
                    Screen.Sharing -> SharingHubScreen(repository = repository)
                }
            }
        }
    }
}

