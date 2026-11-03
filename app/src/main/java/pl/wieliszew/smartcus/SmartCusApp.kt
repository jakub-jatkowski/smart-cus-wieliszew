package pl.wieliszew.smartcus

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.modules.card.WieliszewCardScreen
import pl.wieliszew.smartcus.modules.home.HomeScreen
import pl.wieliszew.smartcus.modules.profile.ProfileScreen
import pl.wieliszew.smartcus.modules.services.CusServicesScreen
import pl.wieliszew.smartcus.modules.volunteer.VolunteerHubScreen
import pl.wieliszew.smartcus.ui.components.BackendSettingsDialog
import pl.wieliszew.smartcus.ui.components.HeaderBanner
import pl.wieliszew.smartcus.ui.components.NotificationsDialog
import pl.wieliszew.smartcus.ui.components.ThemeSelectorDialog
import pl.wieliszew.smartcus.ui.theme.MunicipalTheme
import pl.wieliszew.smartcus.ui.theme.SmartCusTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Pulpit", Icons.Outlined.Home)
    data object Card : Screen("card", "e-Karta", Icons.Outlined.Badge)
    data object Volunteer : Screen("volunteer", "Pomoc", Icons.Outlined.VolunteerActivism)
    data object Services : Screen("services", "Usługi CUS", Icons.Outlined.SupportAgent)
    data object Profile : Screen("profile", "Profil", Icons.Outlined.Person)
}

@Composable
fun SmartCusApp(repository: SmartCusRepository) {
    val isSeniorMode by repository.isSeniorMode.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val isBackendOnline by repository.isBackendOnline.collectAsState()
    val serverUrl by repository.serverUrl.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var currentTheme by remember { mutableStateOf(MunicipalTheme.FOREST) }
    var showThemeSelector by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showBackendSettings by remember { mutableStateOf(false) }

    var volunteerInitialTab by remember { mutableIntStateOf(0) }
    var servicesInitialTab by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        Screen.Home,
        Screen.Card,
        Screen.Volunteer,
        Screen.Services,
        Screen.Profile
    )

    SmartCusTheme(
        isSeniorMode = isSeniorMode,
        selectedTheme = currentTheme
    ) {
        Scaffold(
            topBar = {
                HeaderBanner(
                    title = "CUS Wieliszew",
                    subtitle = "Centrum Usług Społecznych",
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
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
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
                    Screen.Home -> HomeScreen(
                        repository = repository,
                        onNavigateToCard = { currentScreen = Screen.Card },
                        onNavigateToMap = {
                            volunteerInitialTab = 0
                            currentScreen = Screen.Volunteer
                        },
                        onNavigateToTimeBank = {
                            volunteerInitialTab = 1
                            currentScreen = Screen.Volunteer
                        },
                        onNavigateToSharing = {
                            servicesInitialTab = 1
                            currentScreen = Screen.Services
                        },
                        onNavigateToTelehealth = {
                            servicesInitialTab = 0
                            currentScreen = Screen.Services
                        },
                        onNavigateToAssistant = {
                            servicesInitialTab = 2
                            currentScreen = Screen.Services
                        },
                        onNavigateToRewards = {
                            currentScreen = Screen.Profile
                        }
                    )
                    Screen.Card -> WieliszewCardScreen(repository = repository)
                    Screen.Volunteer -> VolunteerHubScreen(
                        repository = repository,
                        initialTab = volunteerInitialTab
                    )
                    Screen.Services -> CusServicesScreen(
                        repository = repository,
                        initialTab = servicesInitialTab,
                        onNavigateToModule = { target ->
                            when (target) {
                                "card" -> currentScreen = Screen.Card
                                "map" -> {
                                    volunteerInitialTab = 0
                                    currentScreen = Screen.Volunteer
                                }
                                "timebank" -> {
                                    volunteerInitialTab = 1
                                    currentScreen = Screen.Volunteer
                                }
                                "sharing" -> {
                                    servicesInitialTab = 1
                                    currentScreen = Screen.Services
                                }
                                "telehealth" -> {
                                    servicesInitialTab = 0
                                    currentScreen = Screen.Services
                                }
                                "gamification", "profile" -> currentScreen = Screen.Profile
                                else -> currentScreen = Screen.Home
                            }
                        }
                    )
                    Screen.Profile -> ProfileScreen(
                        repository = repository,
                        onOpenCard = { currentScreen = Screen.Card },
                        onOpenThemeSelector = { showThemeSelector = true },
                        onOpenBackendSettings = { showBackendSettings = true }
                    )
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
