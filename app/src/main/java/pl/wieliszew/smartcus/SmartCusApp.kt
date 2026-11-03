package pl.wieliszew.smartcus

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import pl.wieliszew.smartcus.ui.components.BackendSettingsDialog
import pl.wieliszew.smartcus.ui.components.HeaderBanner
import pl.wieliszew.smartcus.ui.components.NotificationsDialog
import pl.wieliszew.smartcus.ui.components.ThemeSelectorDialog
import pl.wieliszew.smartcus.ui.theme.MunicipalTheme
import pl.wieliszew.smartcus.ui.theme.SmartCusTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Assistant : Screen("assistant", "Start", Icons.Default.Home)
    data object Card : Screen("card", "e-Karta", Icons.Default.CreditCard)
    data object Map : Screen("map", "Mapa", Icons.Default.Place)
    data object Community : Screen("community", "Wspólnota", Icons.Default.People)
    data object Services : Screen("services", "Usługi", Icons.Default.Widgets)
}

@Composable
fun SmartCusApp(repository: SmartCusRepository) {
    val isSeniorMode by repository.isSeniorMode.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val isBackendOnline by repository.isBackendOnline.collectAsState()
    val serverUrl by repository.serverUrl.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Assistant) }
    var currentTheme by remember { mutableStateOf(MunicipalTheme.FOREST) }
    var showThemeSelector by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showBackendSettings by remember { mutableStateOf(false) }

    var communitySubTab by remember { mutableStateOf("timebank") }
    var servicesSubTab by remember { mutableStateOf("telehealth") }

    val navItems = listOf(
        Screen.Assistant,
        Screen.Card,
        Screen.Map,
        Screen.Community,
        Screen.Services
    )

    SmartCusTheme(
        isSeniorMode = isSeniorMode,
        selectedTheme = currentTheme
    ) {
        Scaffold(
            topBar = {
                HeaderBanner(
                    title = "Smart CUS Wieliszew",
                    subtitle = "Cyfrowy Hub Usług Społecznych",
                    isSeniorMode = isSeniorMode,
                    unreadNotificationsCount = notifications.count { !it.isRead },
                    isBackendOnline = isBackendOnline,
                    onOpenBackendSettings = { showBackendSettings = true },
                    onOpenNotifications = { showNotificationsDialog = true },
                    onOpenThemeSelector = { showThemeSelector = true },
                    onToggleSeniorMode = { repository.toggleSeniorMode() }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 4.dp
                ) {
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = if (isSeniorMode) 11.sp else 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
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
                                "timebank" -> {
                                    communitySubTab = "timebank"
                                    currentScreen = Screen.Community
                                }
                                "sharing" -> {
                                    communitySubTab = "sharing"
                                    currentScreen = Screen.Community
                                }
                                "telehealth" -> {
                                    servicesSubTab = "telehealth"
                                    currentScreen = Screen.Services
                                }
                                "gamification" -> {
                                    servicesSubTab = "gamification"
                                    currentScreen = Screen.Services
                                }
                                "events" -> currentScreen = Screen.Card
                            }
                        }
                    )
                    Screen.Card -> WieliszewCardScreen(repository = repository)
                    Screen.Map -> NeedMapScreen(repository = repository)
                    Screen.Community -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            TabRow(
                                selectedTabIndex = if (communitySubTab == "timebank") 0 else 1,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                Tab(
                                    selected = communitySubTab == "timebank",
                                    onClick = { communitySubTab = "timebank" },
                                    text = {
                                        Text(
                                            text = "Bank Czasu",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = if (isSeniorMode) 13.sp else 12.sp,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                )
                                Tab(
                                    selected = communitySubTab == "sharing",
                                    onClick = { communitySubTab = "sharing" },
                                    text = {
                                        Text(
                                            text = "Jadłodzielnia & Eko",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = if (isSeniorMode) 13.sp else 12.sp,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                )
                            }
                            if (communitySubTab == "timebank") {
                                TimeBankScreen(repository = repository)
                            } else {
                                SharingHubScreen(repository = repository)
                            }
                        }
                    }
                    Screen.Services -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            TabRow(
                                selectedTabIndex = if (servicesSubTab == "telehealth") 0 else 1,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                Tab(
                                    selected = servicesSubTab == "telehealth",
                                    onClick = { servicesSubTab = "telehealth" },
                                    text = {
                                        Text(
                                            text = "Teleopieka",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = if (isSeniorMode) 13.sp else 12.sp,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                )
                                Tab(
                                    selected = servicesSubTab == "gamification",
                                    onClick = { servicesSubTab = "gamification" },
                                    text = {
                                        Text(
                                            text = "Punkty & Nagrody",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = if (isSeniorMode) 13.sp else 12.sp,
                                            maxLines = 1,
                                            softWrap = false,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                )
                            }
                            if (servicesSubTab == "telehealth") {
                                TelehealthScreen(repository = repository)
                            } else {
                                GamificationScreen(
                                    repository = repository,
                                    onNavigateToCard = { currentScreen = Screen.Card }
                                )
                            }
                        }
                    }
                }

                // Modal: Powiadomienia Gminne CUS
                if (showNotificationsDialog) {
                    NotificationsDialog(
                        notifications = notifications,
                        onDismiss = { showNotificationsDialog = false }
                    )
                }

                // Modal: Wybór Motywu Material You
                if (showThemeSelector) {
                    ThemeSelectorDialog(
                        currentTheme = currentTheme,
                        onSelectTheme = { currentTheme = it },
                        onDismiss = { showThemeSelector = false }
                    )
                }

                // Modal: Konfiguracja Serwera Backend CUS API
                if (showBackendSettings) {
                    BackendSettingsDialog(
                        currentUrl = serverUrl,
                        isOnline = isBackendOnline,
                        onSaveUrl = { repository.updateServerUrl(it) },
                        onTestConnection = { repository.syncWithBackend() },
                        onDismiss = { showBackendSettings = false }
                    )
                }
            }
        }
    }
}
