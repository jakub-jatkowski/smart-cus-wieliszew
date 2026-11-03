package pl.wieliszew.smartcus.modules.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import pl.wieliszew.smartcus.data.model.NeedCategory
import pl.wieliszew.smartcus.data.model.NeedPin
import pl.wieliszew.smartcus.data.model.NeedStatus
import pl.wieliszew.smartcus.data.model.Urgency
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.NeedCategoryIcon
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.components.StatusBadge
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

/**
 * Oficjalna Interaktywna Mapa Potrzeb i Mikrowolontariatu CUS Wieliszew
 * Oparta na silniku OpenStreetMap (OSM) z obsługą geolokalizacji, strefy RODO i Material Design 3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeedMapScreen(
    repository: SmartCusRepository
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isSenior = LocalSeniorMode.current

    val pins by repository.needPins.collectAsState()

    var selectedVillage by remember { mutableStateOf("Wszystkie") }
    var selectedCategoryFilter by remember { mutableStateOf<NeedCategory?>(null) }
    var isListView by remember { mutableStateOf(false) }
    var selectedPinForDetail by remember { mutableStateOf<NeedPin?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showCategoryFilterSheet by remember { mutableStateOf(false) }

    val villages = listOf("Wszystkie", "Wieliszew", "Łajski", "Skrzeszew", "Michałów-Reginów", "Kałuszyn", "Komornica")

    val filteredPins = remember(pins, selectedVillage, selectedCategoryFilter) {
        pins.filter { pin ->
            val matchVill = selectedVillage == "Wszystkie" || pin.village.equals(selectedVillage, ignoreCase = true)
            val matchCat = selectedCategoryFilter == null || pin.category == selectedCategoryFilter
            matchVill && matchCat
        }
    }

    // Configure osmdroid user agent
    remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("smart_cus_osm", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    // Interactive MapView instance
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            minZoomLevel = 11.5
            maxZoomLevel = 19.0
            controller.setZoom(13.6)
            controller.setCenter(GeoPoint(52.4485, 20.9785)) // Centrum Gminy Wieliszew
        }
    }

    // Manage MapView lifecycle to prevent memory leaks and freezing
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    // Update Map Overlays whenever filteredPins changes
    LaunchedEffect(filteredPins, selectedPinForDetail) {
        mapView.overlays.clear()

        filteredPins.forEach { pin ->
            val point = GeoPoint(pin.latitude, pin.longitude)

            // RODO Privacy Circle Buffer (200m zone)
            try {
                val circle = Polygon.pointsAsCircle(point, 200.0)
                val bufferPolygon = Polygon(mapView).apply {
                    points = circle
                    val fillColor = when (pin.category) {
                        NeedCategory.SHOPPING -> android.graphics.Color.argb(45, 34, 197, 94)
                        NeedCategory.TRANSPORT -> android.graphics.Color.argb(45, 59, 130, 246)
                        NeedCategory.REPAIR -> android.graphics.Color.argb(45, 249, 115, 22)
                        NeedCategory.COMPANIONSHIP -> android.graphics.Color.argb(45, 168, 85, 247)
                        NeedCategory.PET_CARE -> android.graphics.Color.argb(45, 236, 72, 153)
                        NeedCategory.GARDEN -> android.graphics.Color.argb(45, 16, 185, 129)
                    }
                    fillPaint.color = fillColor
                    outlinePaint.color = android.graphics.Color.argb(120, 46, 125, 50)
                    outlinePaint.strokeWidth = 2.5f
                    title = "Strefa RODO 200m (${pin.village})"
                }
                mapView.overlays.add(bufferPolygon)
            } catch (e: Exception) {
                // Safe fallback if polygon creation fails
            }

            // Interactive Map Marker with custom vector pin
            val isUrgent = pin.urgency == Urgency.TODAY
            val marker = Marker(mapView).apply {
                position = point
                icon = createCustomNeedMarkerDrawable(context, pin.category, isUrgent)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "${pin.category.label}: ${pin.title}"
                snippet = "Sołectwo ${pin.village} • ${pin.urgency.label}"
                setOnMarkerClickListener { _, _ ->
                    selectedPinForDetail = pin
                    true
                }
            }
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Zgłoś potrzebę", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!isListView) {
                // REAL CARTOGRAPHIC OPENSTREETMAP CANVAS
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize()
                )

                // Sleek Single-Row Top Floating Filter Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .align(Alignment.TopCenter),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalIconButton(
                            onClick = { showCategoryFilterSheet = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            BadgedBox(
                                badge = {
                                    if (selectedCategoryFilter != null) {
                                        Badge(containerColor = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterList,
                                    contentDescription = "Filtruj kategorie",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(villages) { vill ->
                                FilterChip(
                                    selected = selectedVillage == vill,
                                    onClick = {
                                        selectedVillage = vill
                                        if (vill != "Wszystkie") {
                                            val (cLat, cLng) = SmartCusRepository.getVillageCoordinates(vill)
                                            mapView.controller.animateTo(GeoPoint(cLat, cLng), 14.5, 800L)
                                        } else {
                                            mapView.controller.animateTo(GeoPoint(52.4485, 20.9785), 13.5, 800L)
                                        }
                                    },
                                    label = { Text(vill, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, softWrap = false) }
                                )
                            }
                        }
                    }
                }

                // Floating Action Map Controls (Bottom End)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    // Toggle to List View
                    FilledTonalButton(
                        onClick = { isListView = true },
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lista (${filteredPins.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                    }

                    // Recenter on Wieliszew Center
                    FloatingActionButton(
                        onClick = {
                            mapView.controller.animateTo(GeoPoint(52.4485, 20.9785), 14.0, 700L)
                        },
                        modifier = Modifier.size(42.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Wycentruj na Wieliszew", modifier = Modifier.size(20.dp))
                    }

                    // Zoom In
                    SmallFloatingActionButton(
                        onClick = { mapView.controller.zoomIn() },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Przybliż mapę")
                    }

                    // Zoom Out
                    SmallFloatingActionButton(
                        onClick = { mapView.controller.zoomOut() },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Oddal mapę")
                    }
                }

                // Safe RODO overlay indicator at bottom-left
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 14.dp, bottom = 14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.72f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Outlined.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Pinezki z buforem RODO 200m",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // SCROLLABLE LIST VIEW MODE
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Zgłoszenia w sołectwach (${filteredPins.size}):",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Button(
                                onClick = { isListView = false },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Otwórz Mapę", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (filteredPins.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Brak zgłoszeń w wybranym filtrze", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                    Text("Wszystkie prośby w tym rejonie zostały zaopiekowane!", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    } else {
                        items(filteredPins) { pin ->
                            NeedPinCard(
                                pin = pin,
                                isSenior = isSenior,
                                onCardClick = { selectedPinForDetail = pin },
                                onAccept = { repository.acceptNeedPin(pin.id) },
                                onComplete = { repository.completeNeedPin(pin.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // ModalBottomSheet for Pin Details (Material Design 3)
    if (selectedPinForDetail != null) {
        val pin = selectedPinForDetail!!
        val isAccepted = pin.status == NeedStatus.IN_PROGRESS
        val isCompleted = pin.status == NeedStatus.COMPLETED

        ModalBottomSheet(
            onDismissRequest = { selectedPinForDetail = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                // Badges row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NeedCategoryIcon(category = pin.category, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        StatusBadge(
                            text = pin.category.label,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    StatusBadge(
                        text = when {
                            isCompleted -> "ZREALIZOWANE"
                            isAccepted -> "W trakcie realizacji"
                            else -> pin.urgency.label
                        },
                        containerColor = when {
                            isCompleted -> Color(0xFF1B5E20)
                            isAccepted -> Color(0xFF2E7D32)
                            else -> Color(0xFFFFE082)
                        },
                        contentColor = when {
                            isCompleted -> Color.White
                            isAccepted -> Color.White
                            else -> Color(0xFF4E342E)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pin.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Sołectwo ${pin.village} • ${pin.approximateAddress}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pin.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+${pin.pointsReward} pkt wolontariatu",
                        color = Color(0xFFF57F17),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )

                    when {
                        isCompleted -> {
                            Text(
                                text = "Pomoc zrealizowana — dziękujemy!",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        isAccepted -> {
                            Button(
                                onClick = {
                                    repository.completeNeedPin(pin.id)
                                    selectedPinForDetail = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Zakończ pomoc", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                            }
                        }
                        else -> {
                            Button(
                                onClick = {
                                    repository.acceptNeedPin(pin.id)
                                    selectedPinForDetail = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Chcę pomóc", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                            }
                        }
                    }
                }
            }
        }
    }

    // Category Filter ModalBottomSheet (Progressive Disclosure)
    if (showCategoryFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategoryFilterSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtruj wg kategorii pomocy",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (selectedCategoryFilter != null) {
                        TextButton(onClick = { selectedCategoryFilter = null }) {
                            Text("Wyczyść filtr", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCategoryFilter = null
                                showCategoryFilterSheet = false
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedCategoryFilter == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Wszystkie kategorie potrzeb", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    NeedCategory.entries.forEach { cat ->
                        val isSelected = selectedCategoryFilter == cat
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategoryFilter = cat
                                    showCategoryFilterSheet = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NeedCategoryIcon(category = cat, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(cat.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Zgłoś potrzebę do koordynatora CUS
    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var village by remember { mutableStateOf("Wieliszew") }
        var category by remember { mutableStateOf(NeedCategory.SHOPPING) }
        var urgency by remember { mutableStateOf(Urgency.TODAY) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AddLocationAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nowe zgłoszenie sąsiedzkie", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Twoje zgłoszenie pojawi się na mapie z 200-metrowym buforem RODO chroniącym prywatność.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tytuł potrzeby") },
                        placeholder = { Text("np. Pomoc w zakupach leków") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Szczegóły prośby") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Text("Sołectwo:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf("Wieliszew", "Łajski", "Skrzeszew", "Michałów-Reginów", "Komornica", "Kałuszyn")) { v ->
                            FilterChip(
                                selected = village == v,
                                onClick = { village = v },
                                label = { Text(v, fontSize = 11.sp, maxLines = 1, softWrap = false) }
                            )
                        }
                    }

                    Text("Kategoria:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(NeedCategory.entries.toTypedArray()) { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat.label, fontSize = 11.sp, maxLines = 1, softWrap = false) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            repository.addNeedPin(
                                title = title,
                                category = category,
                                village = village,
                                description = description,
                                urgency = urgency
                            )
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Opublikuj na mapie", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Anuluj", maxLines = 1, softWrap = false)
                }
            }
        )
    }
}

@Composable
fun NeedPinCard(
    pin: NeedPin,
    isSenior: Boolean,
    onCardClick: () -> Unit,
    onAccept: () -> Unit,
    onComplete: () -> Unit
) {
    val isAccepted = pin.status == NeedStatus.IN_PROGRESS
    val isCompleted = pin.status == NeedStatus.COMPLETED

    SeniorAccessibleCard(onClick = onCardClick) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeedCategoryIcon(category = pin.category, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    StatusBadge(
                        text = pin.category.label,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                StatusBadge(
                    text = when {
                        isCompleted -> "ZREALIZOWANE"
                        isAccepted -> "W trakcie realizacji"
                        else -> pin.urgency.label
                    },
                    containerColor = when {
                        isCompleted -> Color(0xFF1B5E20)
                        isAccepted -> Color(0xFF2E7D32)
                        else -> Color(0xFFFFE082)
                    },
                    contentColor = when {
                        isCompleted -> Color.White
                        isAccepted -> Color.White
                        else -> Color(0xFF4E342E)
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = pin.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Sołectwo ${pin.village} • ${pin.approximateAddress}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = pin.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+${pin.pointsReward} pkt wolontariatu",
                    color = Color(0xFFF57F17),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                )

                when {
                    isCompleted -> {
                        Text(
                            text = "Zrealizowano",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    isAccepted -> {
                        Button(
                            onClick = onComplete,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Zakończ pomoc", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    else -> {
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Chcę pomóc", fontWeight = FontWeight.Bold, maxLines = 1, softWrap = false, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Generuje wektorowy piktogram pinezki M3 dla mapy OpenStreetMap
 * z kolorem kategorii, inicjałem oraz ewentualną czerwoną aureolą pilności
 */
fun createCustomNeedMarkerDrawable(
    context: Context,
    category: NeedCategory,
    isUrgent: Boolean
): Drawable {
    val sizePx = 96
    val bitmap = Bitmap.createBitmap(sizePx, sizePx + 18, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val catColor = when (category) {
        NeedCategory.SHOPPING -> android.graphics.Color.rgb(34, 197, 94)
        NeedCategory.TRANSPORT -> android.graphics.Color.rgb(59, 130, 246)
        NeedCategory.REPAIR -> android.graphics.Color.rgb(249, 115, 22)
        NeedCategory.COMPANIONSHIP -> android.graphics.Color.rgb(168, 85, 247)
        NeedCategory.PET_CARE -> android.graphics.Color.rgb(236, 72, 153)
        NeedCategory.GARDEN -> android.graphics.Color.rgb(16, 185, 129)
    }

    // Subtelny cień
    paint.color = android.graphics.Color.argb(50, 0, 0, 0)
    canvas.drawCircle(sizePx / 2f, sizePx / 2f + 4f, sizePx / 2f - 4f, paint)

    // Zewnętrzny pierścień
    paint.color = if (isUrgent) android.graphics.Color.rgb(220, 38, 38) else android.graphics.Color.WHITE
    canvas.drawCircle(sizePx / 2f, sizePx / 2f, sizePx / 2f - 4f, paint)

    // Wewnętrzne koło w kolorze kategorii
    paint.color = catColor
    canvas.drawCircle(sizePx / 2f, sizePx / 2f, sizePx / 2f - 10f, paint)

    // Iglica wskazująca precyzyjną pozycję geograficzną
    val needlePath = Path().apply {
        moveTo(sizePx / 2f - 10f, sizePx / 2f + 18f)
        lineTo(sizePx / 2f + 10f, sizePx / 2f + 18f)
        lineTo(sizePx / 2f, sizePx + 14f)
        close()
    }
    paint.color = if (isUrgent) android.graphics.Color.rgb(220, 38, 38) else catColor
    canvas.drawPath(needlePath, paint)

    // Inicjał kategorii
    val letter = when (category) {
        NeedCategory.SHOPPING -> "Z"
        NeedCategory.TRANSPORT -> "T"
        NeedCategory.REPAIR -> "N"
        NeedCategory.COMPANIONSHIP -> "P"
        NeedCategory.PET_CARE -> "Z"
        NeedCategory.GARDEN -> "O"
    }
    paint.color = android.graphics.Color.WHITE
    paint.textSize = 34f
    paint.typeface = Typeface.DEFAULT_BOLD
    paint.textAlign = Paint.Align.CENTER
    val yPos = (sizePx / 2f) - ((paint.descent() + paint.ascent()) / 2)
    canvas.drawText(letter, sizePx / 2f, yPos, paint)

    return BitmapDrawable(context.resources, bitmap)
}
