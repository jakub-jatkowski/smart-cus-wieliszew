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
import pl.wieliszew.smartcus.data.model.TelehealthSpecialist
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.AccessibleButton
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.components.StatusBadge
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenPrimary

@Composable
fun TelehealthScreen(
    repository: SmartCusRepository
) {
    val specialists by repository.specialists.collectAsState()
    val isSenior = LocalSeniorMode.current

    var activeCallSpecialist by remember { mutableStateOf<TelehealthSpecialist?>(null) }
    var isMicMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }

    if (activeCallSpecialist != null) {
        // Active WebRTC Video Consultation Screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101413))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar with Security & Specialist Info
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2824)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF81C784), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Szyfrowanie End-to-End CUS", color = Color(0xFF81C784), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(activeCallSpecialist!!.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(activeCallSpecialist!!.type.label, color = Color.LightGray, fontSize = 13.sp)
                        }
                        StatusBadge(text = "LIVE 04:12", containerColor = Color.Red, contentColor = Color.White)
                    }
                }

                // Simulated Main Video Feed
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF263238)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF455A64)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👩‍⚕️", fontSize = 48.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(activeCallSpecialist!!.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Obraz i dźwięk są stabilne", color = Color(0xFF80CBC4), fontSize = 13.sp)
                    }

                    // Self Preview PiP
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(100.dp, 140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isCameraOff) Color.Black else Color(0xFF37474F))
                            .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCameraOff) {
                            Text("Kamera wył.", color = Color.Gray, fontSize = 11.sp)
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🧓", fontSize = 28.sp)
                                Text("Twój obraz", color = Color.White, fontSize = 10.sp)
                            }
                        }
                    }
                }

                // Call Controls Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isMicMuted = !isMicMuted },
                        modifier = Modifier
                            .size(56.dp)
                            .background(if (isMicMuted) Color.Red else Color(0xFF37474F), CircleShape)
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
                            .size(56.dp)
                            .background(if (isCameraOff) Color.Red else Color(0xFF37474F), CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                            contentDescription = "Przełącz kamerę",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { activeCallSpecialist = null },
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFD32F2F), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "Zakończ konsultację",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
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
                .padding(16.dp),
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
                                text = "Telekonsultacje i e-Wsparcie",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bezpłatne, poufne wideorozmowy ze specjalistami CUS Wieliszew",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            items(specialists) { specialist ->
                SpecialistCard(
                    specialist = specialist,
                    isSenior = isSenior,
                    onStartCall = { activeCallSpecialist = specialist }
                )
            }
        }
    }
}

@Composable
fun SpecialistCard(
    specialist: TelehealthSpecialist,
    isSenior: Boolean,
    onStartCall: () -> Unit
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
                    text = if (specialist.availableToday) "DYŻUR DZISIAJ" else "ZAPISY NA JUTRO",
                    containerColor = if (specialist.availableToday) Color(0xFFE8F5E9) else Color(0xFFEDE7F6),
                    contentColor = if (specialist.availableToday) Color(0xFF2E7D32) else Color(0xFF5E35B1)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = specialist.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "🕒 Najbliższy termin: ${specialist.nextFreeSlot}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = specialist.bio,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onStartCall,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (specialist.availableToday) MaterialTheme.colorScheme.primary else Color(0xFF455A64)
                )
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (specialist.availableToday) "Połącz na żywo (Wideo / Audio)" else "Umów termin e-konsultacji",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

