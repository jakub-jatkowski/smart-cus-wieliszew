package pl.wieliszew.smartcus.modules.telehealth

import androidx.compose.foundation.background

import androidx.compose.foundation.border

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay

import pl.wieliszew.smartcus.data.model.TelehealthSpecialist

import pl.wieliszew.smartcus.data.repository.SmartCusRepository

import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard

import pl.wieliszew.smartcus.ui.components.StatusBadge

import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

@Composable

fun TelehealthScreen(

    repository: SmartCusRepository

) {

    val specialists by repository.specialists.collectAsState()

    val isSenior = LocalSeniorMode.current

    var activeCallSpecialist by remember { mutableStateOf<TelehealthSpecialist?>(null) }

    var isMicMuted by remember { mutableStateOf(false) }

    var isCameraOff by remember { mutableStateOf(false) }

    var callSeconds by remember { mutableIntStateOf(0) }

    var showBookingDialogFor by remember { mutableStateOf<TelehealthSpecialist?>(null) }

    var callSummaryMessage by remember { mutableStateOf<String?>(null) }

    // Call timer simulation

    LaunchedEffect(activeCallSpecialist) {

        if (activeCallSpecialist != null) {

            callSeconds = 0

            while (true) {

                delay(1000)

                callSeconds += 1

            }

        }

    }

    if (activeCallSpecialist != null) {

        // Active WebRTC Video Consultation Screen

        val spec = activeCallSpecialist!!

        val minutes = callSeconds / 60

        val seconds = callSeconds % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)

        Box(

            modifier = Modifier

                .fillMaxSize()

                .background(Color(0xFF0F172A))

                .padding(16.dp)

        ) {

            Column(

                modifier = Modifier.fillMaxSize(),

                verticalArrangement = Arrangement.SpaceBetween,

                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                // Top Bar with Security & Specialist Info

                Card(

                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),

                    shape = RoundedCornerShape(16.dp),

                    modifier = Modifier.fillMaxWidth()

                ) {

                    Row(

                        modifier = Modifier.padding(14.dp),

                        verticalAlignment = Alignment.CenterVertically,

                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {

                        Column {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(14.dp))

                                Spacer(modifier = Modifier.width(4.dp))

                                Text("Szyfrowanie End-to-End CUS", color = Color(0xFF4ADE80), fontSize = 11.sp, fontWeight = FontWeight.Bold)

                            }

                            Text(spec.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)

                            Text(spec.type.label, color = Color.LightGray, fontSize = 12.sp)

                        }

                        StatusBadge(text = "LIVE $timeFormatted", containerColor = Color(0xFFDC2626), contentColor = Color.White)

                    }

                }

                // Simulated Main Video Feed

                Box(

                    modifier = Modifier

                        .weight(1f)

                        .fillMaxWidth()

                        .padding(vertical = 14.dp)

                        .clip(RoundedCornerShape(24.dp))

                        .background(Color(0xFF1E2824)),

                    contentAlignment = Alignment.Center

                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Box(

                            modifier = Modifier

                                .size(96.dp)

                                .clip(CircleShape)

                                .background(Color(0xFF334155)),

                            contentAlignment = Alignment.Center

                        ) {

                            Text("👩‍⚕️", fontSize = 48.sp)

                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(spec.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        Text("Obraz i dźwięk HD stabilne (CUS Wieliszew)", color = Color(0xFF86EFAC), fontSize = 12.sp)

                    }

                    // Self Preview PiP

                    Box(

                        modifier = Modifier

                            .align(Alignment.BottomEnd)

                            .padding(14.dp)

                            .size(90.dp, 125.dp)

                            .clip(RoundedCornerShape(14.dp))

                            .background(if (isCameraOff) Color.Black else Color(0xFF475569))

                            .border(2.dp, Color.White, RoundedCornerShape(14.dp)),

                        contentAlignment = Alignment.Center

                    ) {

                        if (isCameraOff) {

                            Text("Kamera wył.", color = Color.Gray, fontSize = 11.sp)

                        } else {

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                Text("🧓", fontSize = 28.sp)

                                Text("Ty", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                            }

                        }

                    }

                }

                // Call Controls Bar

                Row(

                    modifier = Modifier

                        .fillMaxWidth()

                        .padding(bottom = 14.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly,

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    IconButton(

                        onClick = { isMicMuted = !isMicMuted },

                        modifier = Modifier

                            .size(54.dp)

                            .background(if (isMicMuted) Color(0xFFDC2626) else Color(0xFF334155), CircleShape)

                    ) {

                        Icon(

                            imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,

                            contentDescription = "Wycisz mikrofon",

                            tint = Color.White

                        )

                    }

                    IconButton(

                        onClick = { isCameraOff = !isCameraOff },

                        modifier = Modifier

                            .size(54.dp)

                            .background(if (isCameraOff) Color(0xFFDC2626) else Color(0xFF334155), CircleShape)

                    ) {

                        Icon(

                            imageVector = if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,

                            contentDescription = "Przełącz kamerę",

                            tint = Color.White

                        )

                    }

                    IconButton(

                        onClick = {

                            callSummaryMessage = "Konsultacja z ${spec.name} zakończona po czasie: $timeFormatted. Dziękujemy za kontakt z CUS!"

                            activeCallSpecialist = null

                        },

                        modifier = Modifier

                            .size(62.dp)

                            .background(Color(0xFFDC2626), CircleShape)

                    ) {

                        Icon(

                            imageVector = Icons.Default.CallEnd,

                            contentDescription = "Zakończ konsultację",

                            tint = Color.White,

                            modifier = Modifier.size(30.dp)

                        )

                    }

                }

            }

        }

    } else {

        // Specialist List and Schedule

        LazyColumn(

            modifier = Modifier

                .fillMaxSize()

                .background(MaterialTheme.colorScheme.background)

                .padding(horizontal = 16.dp),

            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

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

                            Text("🩺", fontSize = 24.sp)

                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {

                            Text(

                                text = "Teleopieka i e-Wsparcie",

                                style = MaterialTheme.typography.titleLarge,

                                fontWeight = FontWeight.Bold

                            )

                            Text(

                                text = "Bezpłatne, poufne wideorozmowy ze specjalistami CUS Wieliszew",

                                style = MaterialTheme.typography.bodyMedium,

                                color = MaterialTheme.colorScheme.onSurfaceVariant

                            )

                        }

                    }

                }

            }

            items(specialists) { specialist ->

                SpecialistCard(

                    specialist = specialist,

                    isSenior = isSenior,

                    onStartCall = { activeCallSpecialist = specialist },

                    onBookAppointment = { showBookingDialogFor = specialist }

                )

            }

        }

    }

    // Modal: Rezerwacja terminu

    if (showBookingDialogFor != null) {

        val spec = showBookingDialogFor!!

        var selectedSlot by remember { mutableStateOf(spec.nextFreeSlot) }

        var userPhone by remember { mutableStateOf("+48 601 234 567") }

        var isBooked by remember { mutableStateOf(false) }

        AlertDialog(

            onDismissRequest = { showBookingDialogFor = null },

            title = {

                Text("Rezerwacja e-Konsultacji", fontWeight = FontWeight.Bold)

            },

            text = {

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text("Specjalista: ${spec.name}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    Text("Rola: ${spec.type.label}", fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(

                        value = selectedSlot,

                        onValueChange = { selectedSlot = it },

                        label = { Text("Wybrana data i godzina") },

                        modifier = Modifier.fillMaxWidth()

                    )

                    OutlinedTextField(

                        value = userPhone,

                        onValueChange = { userPhone = it },

                        label = { Text("Telefon do powiadomień SMS") },

                        modifier = Modifier.fillMaxWidth()

                    )

                    Text("Konsultacja odbędzie się przez tę aplikację – otrzymasz przypomnienie 15 minut przed terminem.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                }

            },

            confirmButton = {

                Button(

                    onClick = {

                        isBooked = true

                        callSummaryMessage = "Pomyślnie zarezerwowano e-konsultację z: ${spec.name} na termin: $selectedSlot!"

                        showBookingDialogFor = null

                    }

                ) {

                    Text("Zatwierdź rezerwację", fontWeight = FontWeight.Bold)

                }

            },

            dismissButton = {

                TextButton(onClick = { showBookingDialogFor = null }) {

                    Text("Anuluj")

                }

            }

        )

    }

    // Modal: Podsumowanie połączenia / rezerwacji

    if (callSummaryMessage != null) {

        AlertDialog(

            onDismissRequest = { callSummaryMessage = null },

            title = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Potwierdzenie CUS", fontWeight = FontWeight.Bold)

                }

            },

            text = { Text(callSummaryMessage!!) },

            confirmButton = {

                TextButton(onClick = { callSummaryMessage = null }) {

                    Text("OK", fontWeight = FontWeight.Bold)

                }

            }

        )

    }

}

@Composable

fun SpecialistCard(

    specialist: TelehealthSpecialist,

    isSenior: Boolean,

    onStartCall: () -> Unit,

    onBookAppointment: () -> Unit

) {

    SeniorAccessibleCard {

        Column {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                StatusBadge(

                    text = specialist.type.label,

                    containerColor = MaterialTheme.colorScheme.primaryContainer,

                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer

                )

                StatusBadge(

                    text = if (specialist.availableToday) "DYŻUR TERAZ" else "ZAPISY NA JUTRO",

                    containerColor = if (specialist.availableToday) Color(0xFFE8F5E9) else Color(0xFFEDE7F6),

                    contentColor = if (specialist.availableToday) Color(0xFF2E7D32) else Color(0xFF5E35B1)

                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(

                text = specialist.name,

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold

            )

            Text(

                text = "🕒 Wolny termin: ${specialist.nextFreeSlot}",

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.primary,

                fontWeight = FontWeight.SemiBold

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(

                text = specialist.bio,

                style = MaterialTheme.typography.bodyMedium

            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {

                Button(

                    onClick = onStartCall,

                    modifier = Modifier.weight(1f),

                    colors = ButtonDefaults.buttonColors(

                        containerColor = if (specialist.availableToday) MaterialTheme.colorScheme.primary else Color(0xFF475569)

                    )

                ) {

                    Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(18.dp))

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(

                        text = if (specialist.availableToday) "Połącz na żywo" else "Połącz z dyżurem",

                        fontWeight = FontWeight.Bold,

                        maxLines = 1,

                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

                    )

                }

                OutlinedButton(

                    onClick = onBookAppointment

                ) {

                    Text("Umów termin", fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)

                }

            }

        }

    }

}

