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
import androidx.compose.material.icons.outlined.*
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

/**
 * Cyfrowy Punkt Informacyjny i Doradca Mieszkańca CUS Wieliszew
 * Zaprojektowany zgodnie ze standardami Material Design 3 i dostępności WCAG 2.1 AAA
 */
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
        "Kryteria pomocy społecznej 2026",
        "Wypożyczenie sprzętu medycznego",
        "Zasady gminnej Jadłodzielni",
        "Jak wyrobić e-Kartę Mieszkańca?",
        "Pomoc sąsiedzka w zakupach",
        "Dyżury psychologa i radcy prawnego"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Górny pasek statusu punktu informacyjnego
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
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.SupportAgent,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Punkt Informacyjny CUS",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isVoiceActive) "Nagrywanie pytania..." else "Weryfikacja procedur, świadczeń i ulg",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row {
                        IconButton(
                            onClick = { repository.clearChat() },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.RestartAlt,
                                contentDescription = "Zacznij od nowa",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = {
                                isVoiceActive = !isVoiceActive
                                if (isVoiceActive) {
                                    inputText = "Jakie są kryteria dochodowe do pomocy społecznej w 2026 roku?"
                                }
                            },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = if (isVoiceActive) Icons.Filled.Mic else Icons.Outlined.Mic,
                                contentDescription = "Wprowadzanie głosowe",
                                modifier = Modifier.size(20.dp),
                                tint = if (isVoiceActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (isVoiceActive) {
                    VoiceWaveformBar()
                }
            }
        }

        // Lista wiadomości
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

        // Szybkie pytania mieszkańców
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                SuggestionChip(
                    onClick = {
                        repository.sendUserMessage(question)
                    },
                    label = {
                        Text(
                            text = question,
                            fontSize = if (isSenior) 13.sp else 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Pasek wprowadzania pytania
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
                            text = if (isSenior) "Wpisz lub podyktuj pytanie..." else "Zapytaj o świadczenia, procedury, e-Kartę...",
                            fontSize = if (isSenior) 14.sp else 12.sp
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
                                imageVector = Icons.Outlined.Mic,
                                contentDescription = "Podyktuj",
                                tint = if (isVoiceActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
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
                    modifier = Modifier.size(if (isSenior) 54.dp else 46.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Wyślij pytanie"
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
            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f))
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
                    .background(MaterialTheme.colorScheme.error)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Słucham... Wypowiedz pytanie",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
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
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.SupportAgent,
                        contentDescription = "Doradca CUS",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
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
                                    imageVector = if (isPlaying) Icons.Filled.VolumeUp else Icons.Outlined.VolumeUp,
                                    contentDescription = "Odsłuchaj",
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

            if (message.recommendations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth(0.88f)
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
                                        fontSize = if (isSenior) 16.sp else 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = rec.subtitle,
                                        fontSize = if (isSenior) 13.sp else 11.sp,
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
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Mieszkaniec",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
