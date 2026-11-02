package pl.wieliszew.smartcus.modules.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
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

    val quickQuestions = listOf(
        "Jak wyrobić e-Kartę?",
        "Gdzie jest wolny wózek inwalidzki?",
        "Potrzebuję pomocy w zakupach",
        "Kiedy dyżuruje psycholog?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Voice Assistant Header Status
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isSenior) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(if (isVoiceActive) Color.Red else Color(0xFF2E7D32))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVoiceActive) "Voicebot słucha Twojego głosu..." else "Asystent AI CUS Wieliszew (Aktywny 24/7)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = { isVoiceActive = !isVoiceActive }) {
                    Icon(
                        imageVector = if (isVoiceActive) Icons.Default.VolumeUp else Icons.Default.Mic,
                        contentDescription = "Sterowanie głosowe",
                        tint = MaterialTheme.colorScheme.primary
                    )
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
                    onRecommendationClick = { onNavigateToModule(it) }
                )
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                FilterChip(
                    selected = false,
                    onClick = { repository.sendUserMessage(question) },
                    label = {
                        Text(
                            text = question,
                            fontSize = if (isSenior) 15.sp else 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                )
            }
        }

        // Input Bar
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
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
                            text = if (isSenior) "Wpisz lub podyktuj pytanie..." else "Zapytaj o usługi, pomoc, e-Kartę...",
                            fontSize = if (isSenior) 16.sp else 14.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            isVoiceActive = !isVoiceActive
                            if (isVoiceActive) {
                                inputText = "Chciałabym zapisać się na zajęcia z kręgosłupa"
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
fun ChatMessageItem(
    message: AiMessage,
    isSenior: Boolean,
    onRecommendationClick: (String) -> Unit
) {
    val isUser = message.sender == MessageSender.USER
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        if (isSenior) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 320.dp)
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
                Text(
                    text = message.timestamp,
                    color = textColor.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        // Recommendations cards attached to bot reply
        if (message.recommendations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.widthIn(max = 320.dp)
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
                                    fontSize = if (isSenior) 18.sp else 15.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = rec.subtitle,
                                    fontSize = if (isSenior) 15.sp else 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            AssistChip(
                                onClick = { onRecommendationClick(rec.targetModule) },
                                label = { Text(text = rec.reasonBadge, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }
            }
        }
    }
}

