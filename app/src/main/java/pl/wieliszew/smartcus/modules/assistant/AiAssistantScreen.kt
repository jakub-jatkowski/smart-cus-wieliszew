package pl.wieliszew.smartcus.modules.assistant

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import pl.wieliszew.smartcus.data.model.AiMessage
import pl.wieliszew.smartcus.data.model.MessageSender
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    repository: SmartCusRepository,
    onNavigateToModule: (String) -> Unit
) {
    val messages by repository.chatMessages.collectAsState()
    val isSenior = LocalSeniorMode.current
    var inputText by remember { mutableStateOf("") }
    var isVoiceActive by remember { mutableStateOf(false) }
    var playingMessageId by remember { mutableStateOf<String?>(null) }

    val quickQuestions = listOf(
        "📋 Kryteria CUS 2026",
        "♿ Wypożyczalnia sprzętu",
        "🥗 Jadłodzielnia Wieliszew",
        "💳 Jak wyrobić e-Kartę?",
        "🛒 Pomoc w zakupach",
        "🩺 Dyżur psychologa CUS"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Voice Assistant Header Status Bar
        Surface(
            color = if (isSenior) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isVoiceActive) Color.Red else Color(0xFF2E7D32))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isVoiceActive) "Voicebot słucha..." else "Wirtualny Doradca AI CUS 24/7",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row {
                        IconButton(
                            onClick = { repository.clearChat() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Wyczyść czat",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = {
                                isVoiceActive = !isVoiceActive
                                if (isVoiceActive) {
                                    inputText = "Jakie są kryteria dochodowe CUS w 2026 roku?"
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isVoiceActive) Icons.Default.VolumeUp else Icons.Default.Mic,
                                contentDescription = "Sterowanie głosowe",
                                modifier = Modifier.size(18.dp),
                                tint = if (isVoiceActive) Color.Red else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Voice Waveform Animation when voice is active
                if (isVoiceActive) {
                    VoiceWaveformBar()
                }
            }
        }

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatMessageItem(
                    message = msg,
                    isSenior = isSenior,
                    isPlaying = playingMessageId == msg.id,
                    onPlayTts = {
                        playingMessageId = if (playingMessageId == msg.id) null else msg.id
                    },
                    onRecommendationClick = { onNavigateToModule(it) }
                )
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                SuggestionChip(
                    onClick = {
                        val clean = question.replace(Regex("^[^\\sa-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9]+"), "").trim()
                        repository.sendUserMessage(clean)
                    },
                    label = {
                        Text(
                            text = question,
                            fontSize = if (isSenior) 14.sp else 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Input Bar
        Surface(
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (isSenior) "Wpisz lub podyktuj pytanie..." else "Zapytaj o zasiłki, e-Kartę, sprzęt...",
                            fontSize = if (isSenior) 15.sp else 13.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            isVoiceActive = !isVoiceActive
                            if (isVoiceActive) {
                                inputText = "Gdzie znajduje się gminna jadłodzielnia w Wieliszewie?"
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Podyktuj",
                                tint = if (isVoiceActive) Color.Red else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            repository.sendUserMessage(inputText)
                            inputText = ""
                            isVoiceActive = false
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(if (isSenior) 56.dp else 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Wyślij wiadomość"
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceWaveformBar() {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val h1 by infiniteTransition.animateFloat(initialValue = 8f, targetValue = 28f, animationSpec = infiniteRepeatable(tween(400, easing = LinearEasing), RepeatMode.Reverse), label = "h1")
    val h2 by infiniteTransition.animateFloat(initialValue = 18f, targetValue = 36f, animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse), label = "h2")
    val h3 by infiniteTransition.animateFloat(initialValue = 28f, targetValue = 10f, animationSpec = infiniteRepeatable(tween(450, easing = LinearEasing), RepeatMode.Reverse), label = "h3")
    val h4 by infiniteTransition.animateFloat(initialValue = 14f, targetValue = 32f, animationSpec = infiniteRepeatable(tween(380, easing = LinearEasing), RepeatMode.Reverse), label = "h4")
    val h5 by infiniteTransition.animateFloat(initialValue = 24f, targetValue = 12f, animationSpec = infiniteRepeatable(tween(420, easing = LinearEasing), RepeatMode.Reverse), label = "h5")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red.copy(alpha = 0.08f))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(h1, h2, h3, h4, h5, h2, h4).forEach { height ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Red)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Mów teraz... Polski Voicebot CUS przetwarza mowę", fontSize = 11.sp, color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ChatMessageItem(
    message: AiMessage,
    isSenior: Boolean,
    isPlaying: Boolean,
    onPlayTts: () -> Unit,
    onRecommendationClick: (String) -> Unit
) {
    val isUser = message.sender == MessageSender.USER
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        if (isSenior) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("🤖", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .background(bubbleColor)
                    .then(
                        if (isSenior && !isUser) Modifier.border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)) else Modifier
                    )
                    .padding(if (isSenior) 16.dp else 12.dp)
            ) {
                Column {
                    Text(
                        text = message.text,
                        color = textColor,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = if (isSenior) 26.sp else 22.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isUser) {
                            IconButton(
                                onClick = onPlayTts,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                    contentDescription = "Odtwórz na głos",
                                    tint = if (isPlaying) MaterialTheme.colorScheme.primary else textColor.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }
                        Text(
                            text = message.timestamp,
                            color = textColor.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            // Recommendations cards attached to bot reply
            if (message.recommendations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.widthIn(max = 300.dp)
                ) {
                    message.recommendations.forEach { rec ->
                        SeniorAccessibleCard(
                            onClick = { onRecommendationClick(rec.targetModule) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rec.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isSenior) 17.sp else 14.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = rec.subtitle,
                                        fontSize = if (isSenior) 14.sp else 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                AssistChip(
                                    onClick = { onRecommendationClick(rec.targetModule) },
                                    label = { Text(text = rec.reasonBadge, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 16.sp)
            }
        }
    }
}
