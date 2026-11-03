package pl.wieliszew.smartcus.modules.gamification

import androidx.compose.foundation.background

import androidx.compose.foundation.border

import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.CardGiftcard

import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.material.icons.filled.EmojiEvents

import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import pl.wieliszew.smartcus.data.model.Badge

import pl.wieliszew.smartcus.data.model.GamificationReward

import pl.wieliszew.smartcus.data.repository.SmartCusRepository

import pl.wieliszew.smartcus.ui.components.SeniorAccessibleCard

import pl.wieliszew.smartcus.ui.components.StatusBadge

import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

import pl.wieliszew.smartcus.ui.theme.WieliszewGold

@Composable

fun GamificationScreen(

    repository: SmartCusRepository,

    onNavigateToCard: (() -> Unit)? = null

) {

    val userProfile by repository.userProfile.collectAsState()

    val badges by repository.badges.collectAsState()

    val rewards by repository.rewards.collectAsState()

    val isSenior = LocalSeniorMode.current

    var selectedBadgeForDetail by remember { mutableStateOf<Badge?>(null) }

    var claimedRewardDialog by remember { mutableStateOf<GamificationReward?>(null) }

    LazyColumn(

        modifier = Modifier

            .fillMaxSize()

            .background(MaterialTheme.colorScheme.background)

            .padding(horizontal = 16.dp),

        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        // Points & Level Banner

        item {

            Box(

                modifier = Modifier

                    .fillMaxWidth()

                    .clip(RoundedCornerShape(20.dp))

                    .background(if (isSenior) Color(0xFF1E1E1E) else MaterialTheme.colorScheme.primary)

                    .then(

                        if (isSenior) Modifier.border(3.dp, WieliszewGold, RoundedCornerShape(20.dp)) else Modifier

                    )

                    .padding(20.dp)

            ) {

                Column {

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement = Arrangement.SpaceBetween,

                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Column {

                            Text(

                                text = "POZIOM ZAANGAŻOWANIA SPOŁECZNEGO",

                                color = WieliszewGold,

                                fontWeight = FontWeight.Bold,

                                fontSize = 11.sp,

                                letterSpacing = 1.sp

                            )

                            Text(

                                text = userProfile.levelTitle,

                                color = Color.White,

                                fontWeight = FontWeight.ExtraBold,

                                fontSize = if (isSenior) 22.sp else 19.sp

                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Icon(Icons.Default.Star, contentDescription = null, tint = WieliszewGold, modifier = Modifier.size(20.dp))

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(

                                    text = "${userProfile.gamificationPoints} pkt aktywności",

                                    color = Color.White,

                                    fontWeight = FontWeight.Bold,

                                    fontSize = 16.sp

                                )

                            }

                        }

                        Box(

                            modifier = Modifier

                                .size(56.dp)

                                .clip(CircleShape)

                                .background(WieliszewGold),

                            contentAlignment = Alignment.Center

                        ) {

                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.Black, modifier = Modifier.size(32.dp))

                        }

                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Level Progress Bar

                    val nextLevelTarget = 500

                    val progress = (userProfile.gamificationPoints.toFloat() / nextLevelTarget).coerceIn(0f, 1f)

                    Column {

                        Row(

                            modifier = Modifier.fillMaxWidth(),

                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {

                            Text(

                                text = "Postęp do poziomu: Lider Społeczności",

                                color = Color.White.copy(alpha = 0.85f),

                                fontSize = 11.sp

                            )

                            Text(

                                text = "${userProfile.gamificationPoints} / $nextLevelTarget pkt",

                                color = WieliszewGold,

                                fontWeight = FontWeight.Bold,

                                fontSize = 11.sp

                            )

                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(

                            progress = { progress },

                            modifier = Modifier

                                .fillMaxWidth()

                                .height(8.dp)

                                .clip(RoundedCornerShape(4.dp)),

                            color = WieliszewGold,

                            trackColor = Color.White.copy(alpha = 0.25f)

                        )

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

                    fontWeight = FontWeight.Bold,

                    color = MaterialTheme.colorScheme.onSurface

                )

                Text(

                    text = "Dotknij odznaki, aby sprawdzić warunki jej zdobycia",

                    style = MaterialTheme.typography.bodySmall,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(

                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {

                    items(badges) { badge ->

                        BadgeItem(

                            badge = badge,

                            isSenior = isSenior,

                            onClick = { selectedBadgeForDetail = badge }

                        )

                    }

                }

            }

        }

        // Section: Katalog Nagród i Benefitów

        item {

            Text(

                text = "Katalog Nagród i Zniżek Partnerów",

                style = MaterialTheme.typography.titleLarge,

                fontWeight = FontWeight.Bold,

                color = MaterialTheme.colorScheme.primary

            )

        }

        items(rewards) { reward ->

            RewardCard(

                reward = reward,

                userPoints = userProfile.gamificationPoints,

                isSenior = isSenior,

                onRedeem = {

                    val success = repository.redeemReward(reward.id)

                    if (success) {

                        claimedRewardDialog = reward

                    }

                }

            )

        }

    }

    // Modal: Szczegóły odznaki

    if (selectedBadgeForDetail != null) {

        val badge = selectedBadgeForDetail!!

        AlertDialog(

            onDismissRequest = { selectedBadgeForDetail = null },

            title = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(badge.iconEmoji, fontSize = 28.sp)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(badge.title, fontWeight = FontWeight.Bold)

                }

            },

            text = {

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    StatusBadge(

                        text = if (badge.isUnlocked) "ODBLOKOWANA (${badge.unlockedDate})" else "DO ZDOBYCIA",

                        containerColor = if (badge.isUnlocked) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),

                        contentColor = if (badge.isUnlocked) Color(0xFF2E7D32) else Color.Gray

                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = badge.description, style = MaterialTheme.typography.bodyMedium)

                }

            },

            confirmButton = {

                TextButton(onClick = { selectedBadgeForDetail = null }) {

                    Text("Rozumiem", fontWeight = FontWeight.Bold)

                }

            }

        )

    }

    // Modal: Nagroda pomyślnie odebrana

    if (claimedRewardDialog != null) {

        val rew = claimedRewardDialog!!

        AlertDialog(

            onDismissRequest = { claimedRewardDialog = null },

            title = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Nagroda odebrana! 🎉", fontWeight = FontWeight.Bold)

                }

            },

            text = {

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text(

                        text = rew.title,

                        fontWeight = FontWeight.Bold,

                        style = MaterialTheme.typography.titleMedium,

                        color = MaterialTheme.colorScheme.primary

                    )

                    Text("Partner: ${rew.partnerName}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(

                        text = "Voucher został automatycznie zapisany w Twojej e-Karcie Mieszkańca z unikalnym kodem zniżkowym.",

                        style = MaterialTheme.typography.bodyMedium,

                        color = MaterialTheme.colorScheme.onSurfaceVariant

                    )

                }

            },

            confirmButton = {

                Button(

                    onClick = {

                        claimedRewardDialog = null

                        onNavigateToCard?.invoke()

                    }

                ) {

                    Text("Pokaż w e-Karcie", fontWeight = FontWeight.Bold)

                }

            },

            dismissButton = {

                TextButton(onClick = { claimedRewardDialog = null }) {

                    Text("Zostań tutaj")

                }

            }

        )

    }

}

@Composable

fun BadgeItem(

    badge: Badge,

    isSenior: Boolean,

    onClick: () -> Unit

) {

    Box(

        modifier = Modifier

            .width(135.dp)

            .clip(RoundedCornerShape(16.dp))

            .background(if (badge.isUnlocked) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainer)

            .border(

                width = if (badge.isUnlocked) 2.dp else 1.dp,

                color = if (badge.isUnlocked) WieliszewGold else MaterialTheme.colorScheme.outlineVariant,

                shape = RoundedCornerShape(16.dp)

            )

            .clickable { onClick() }

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

                maxLines = 1,

                textAlign = TextAlign.Center

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

                    fontSize = 15.sp,

                    color = if (canAfford) Color(0xFFF57F17) else Color.Gray

                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = reward.title,

                style = MaterialTheme.typography.titleMedium,

                fontWeight = FontWeight.Bold

            )

            Text(

                text = "Partner: ${reward.partnerName}",

                style = MaterialTheme.typography.bodySmall,

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

                    text = if (canAfford) "Odbierz za ${reward.pointsCost} pkt" else "Brakuje ${reward.pointsCost - userPoints} pkt",

                    fontWeight = FontWeight.Bold,

                    maxLines = 1,

                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis

                )

            }

        }

    }

}

