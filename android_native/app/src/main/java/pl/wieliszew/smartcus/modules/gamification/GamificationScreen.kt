package pl.wieliszew.smartcus.modules.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.data.model.Badge
import pl.wieliszew.smartcus.data.model.GamificationReward
import pl.wieliszew.smartcus.data.repository.SmartCusRepository
import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard
import pl.wieliszew.smartcus.ui.components.StatusBadge
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode
import pl.wieliszew.smartcus.ui.theme.WieliszewGold
import pl.wieliszew.smartcus.ui.theme.WieliszewGreenPrimary

@Composable
fun GamificationScreen(
    repository: SmartCusRepository
) {
    val userProfile by repository.userProfile.collectAsState()
    val badges by repository.badges.collectAsState()
    val rewards by repository.rewards.collectAsState()
    val isSenior = LocalSeniorMode.current

    var redeemMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Points & Level Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isSenior) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.primary)
                    .then(
                        if (isSenior) Modifier.border(3.dp, WieliszewGold, RoundedCornerShape(18.dp)) else Modifier
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "POZIOM SPOŁECZNY",
                            color = WieliszewGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = userProfile.levelTitle,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (isSenior) 22.sp else 19.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = WieliszewGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userProfile.gamificationPoints} pkt zaangażowania",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(WieliszewGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(36.dp))
                    }
                }
            }
        }

        // Section: Odznaki Mieszkańca
        item {
            Column {
                Text(
                    text = "Twoje Odznaki Społeczne",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(badges) { badge ->
                        BadgeItem(badge = badge, isSenior = isSenior)
                    }
                }
            }
        }

        // Section: Katalog Nagród i Benefitów
        item {
            Text(
                text = "Katalog Nagród i Zniżek Partnerów",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (redeemMessage != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎉 ", fontSize = 20.sp)
                        Text(
                            text = redeemMessage!!,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        items(rewards) { reward ->
            RewardCard(
                reward = reward,
                userPoints = userProfile.gamificationPoints,
                isSenior = isSenior,
                onRedeem = {
                    val success = repository.redeemReward(reward.id)
                    if (success) {
                        redeemMessage = "Pomyślnie odebrano: ${reward.title}! Kod kreskowy zapisano w e-Karcie."
                    }
                }
            )
        }
    }
}

@Composable
fun BadgeItem(badge: Badge, isSenior: Boolean) {
    Box(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (badge.isUnlocked) MaterialTheme.colorScheme.surface else Color(0xFFF0F0F0))
            .border(
                width = if (badge.isUnlocked) 2.dp else 1.dp,
                color = if (badge.isUnlocked) WieliszewGold else Color.LightGray,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(badge.iconEmoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = badge.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1
            )
            Text(
                text = if (badge.isUnlocked) "Odblokowana" else "Do zdobycia",
                fontSize = 10.sp,
                color = if (badge.isUnlocked) Color(0xFF2E7D32) else Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RewardCard(
    reward: GamificationReward,
    userPoints: Int,
    isSenior: Boolean,
    onRedeem: () -> Unit
) {
    val canAfford = userPoints >= reward.pointsCost

    SeniorAccessibleCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(
                    text = reward.category,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${reward.pointsCost} pkt",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = if (canAfford) Color(0xFFF57F17) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reward.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Partner: ${reward.partnerName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = reward.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onRedeem,
                enabled = canAfford,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (canAfford) "Odbierz za ${reward.pointsCost} pkt" else "Brakuje Ci ${reward.pointsCost - userPoints} pkt",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

