package pl.wieliszew.smartcus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wieliszew.smartcus.ui.theme.LocalSeniorMode

@Composable
fun SeniorAccessibleCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isSenior = LocalSeniorMode.current
    val border = if (isSenior) BorderStroke(3.dp, MaterialTheme.colorScheme.outline) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    val shape = RoundedCornerShape(if (isSenior) 12.dp else 16.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSenior) 0.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (isSenior) 20.dp else 16.dp),
            content = content
        )
    }
}

@Composable
fun AccessibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    val isSenior = LocalSeniorMode.current
    val height = if (isSenior) 58.dp else 48.dp

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(if (isSenior) 10.dp else 24.dp),
        colors = if (isPrimary) {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        },
        border = if (!isPrimary || isSenior) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val isSenior = LocalSeniorMode.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .padding(horizontal = if (isSenior) 12.dp else 8.dp, vertical = if (isSenior) 6.dp else 4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = if (isSenior) 14.sp else 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HeaderBanner(
    title: String,
    subtitle: String,
    isSeniorMode: Boolean,
    onToggleSeniorMode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            }

            // Senior / High Contrast Mode Quick Toggle
            FilledTonalButton(
                onClick = onToggleSeniorMode,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isSeniorMode) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isSeniorMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(
                    text = if (isSeniorMode) "Aa Normalny" else "Aa Tryb Seniora",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

