package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.audio.PupSoundEffects
import com.example.model.PupCharacter
import com.example.ui.theme.PawBoneWhite
import com.example.ui.theme.PawCardBorder
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawFireOrange
import com.example.ui.theme.PawSunYellow
import com.example.ui.theme.PawYellow
import kotlinx.coroutines.launch

@Composable
fun PupTopAppBar(
    cadetName: String,
    starsCount: Int,
    treatsCount: Int,
    currentCompanion: PupCharacter,
    isSoundEnabled: Boolean,
    onCompanionClick: () -> Unit,
    onTrophyClick: () -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onParentalSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
        color = Color.White,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Cadet Avatar & Companion
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(currentCompanion.themeColor.copy(alpha = 0.15f))
                    .clickable {
                        PupSoundEffects.playPop()
                        onCompanionClick()
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("companion_badge_button")
            ) {
                Image(
                    painter = painterResource(id = currentCompanion.imageResId),
                    contentDescription = currentCompanion.pupName,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = cadetName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PawDarkText
                    )
                    Text(
                        text = "Pup: ${currentCompanion.pupName} 🐾",
                        fontSize = 11.sp,
                        color = currentCompanion.darkColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Counters and Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Stars pill
                CounterPill(
                    iconText = "⭐",
                    count = starsCount,
                    bgColor = PawSunYellow,
                    tag = "stars_counter_pill"
                )

                // Bones pill
                CounterPill(
                    iconText = "🦴",
                    count = treatsCount,
                    bgColor = PawBoneWhite,
                    tag = "treats_counter_pill"
                )

                // Trophies / Badges
                IconButton(
                    onClick = {
                        PupSoundEffects.playPop()
                        onTrophyClick()
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .background(PawSunYellow.copy(alpha = 0.3f), CircleShape)
                        .testTag("trophy_room_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophies",
                        tint = PawFireOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Sound Toggle
                IconButton(
                    onClick = { onSoundToggle(!isSoundEnabled) },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFECEFF1), CircleShape)
                        .testTag("sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Sound Toggle",
                        tint = PawDarkText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Parental lock settings
                IconButton(
                    onClick = onParentalSettingsClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFE0E0E0), CircleShape)
                        .testTag("parental_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Parental Settings",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CounterPill(
    iconText: String,
    count: Int,
    bgColor: Color,
    tag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.5.dp, PawCardBorder),
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .testTag(tag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = iconText, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$count",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = PawDarkText
            )
        }
    }
}

@Composable
fun PupBouncyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = PawFireOrange,
    textColor: Color = Color.White,
    fontSize: Int = 18,
    iconEmoji: String = "",
    testTag: String = "pup_bouncy_button"
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .scale(scale.value)
            .shadow(6.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        color,
                        color.copy(alpha = 0.85f)
                    )
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        PupSoundEffects.playPop()
                        scope.launch {
                            scale.animateTo(0.92f, spring(stiffness = 800f))
                        }
                        tryAwaitRelease()
                        scope.launch {
                            scale.animateTo(1f, spring(stiffness = 600f))
                        }
                        onClick()
                    }
                )
            }
            .padding(horizontal = 24.dp, vertical = 14.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconEmoji.isNotEmpty()) {
                Text(text = iconEmoji, fontSize = (fontSize + 4).sp)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CelebrationDialog(
    isOpen: Boolean,
    title: String,
    subtitle: String,
    companion: PupCharacter,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val infiniteTransition = rememberInfiniteTransition(label = "stars_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(32.dp))
                .testTag("celebration_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sparkling Stars Header
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "✨ 🌟 ✨",
                        fontSize = 36.sp,
                        modifier = Modifier.rotate(rotation)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Pup companion avatar
                Image(
                    painter = painterResource(id = companion.imageResId),
                    contentDescription = companion.pupName,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(4.dp, companion.themeColor), CircleShape)
                        .shadow(6.dp, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = companion.darkColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PawDarkText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                PupBouncyButton(
                    text = "High Five! Keep Playing! 🐾",
                    onClick = onDismiss,
                    color = companion.themeColor,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "celebration_continue_button"
                )
            }
        }
    }
}

@Composable
fun CompanionSelectorDialog(
    isOpen: Boolean,
    currentCompanion: PupCharacter,
    onCompanionSelected: (PupCharacter) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("companion_selector_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pick Your Rescue Companion!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PawDarkText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PupCharacter.entries.forEach { pup ->
                        val isSelected = pup == currentCompanion
                        Card(
                            onClick = { onCompanionSelected(pup) },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) pup.themeColor.copy(alpha = 0.2f) else Color(0xFFF5F5F5)
                            ),
                            border = if (isSelected) BorderStroke(2.dp, pup.themeColor) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("select_pup_${pup.pupName.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = pup.imageResId),
                                    contentDescription = pup.pupName,
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = pup.pupName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = pup.darkColor
                                    )
                                    Text(
                                        text = pup.roleTitle,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "\"${pup.catchphrase}\"",
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                if (isSelected) {
                                    Text("⭐", fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_companion_picker")
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ParentalGateDialog(
    isOpen: Boolean,
    onConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val num1 = remember { (2..5).random() }
    val num2 = remember { (1..4).random() }
    val answer = num1 + num2
    var selectedOption by remember { mutableIntStateOf(-1) }
    var hasError by remember { mutableStateOf(false) }

    val options = remember {
        listOf(answer, answer + 1, answer - 1).shuffled()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Grown-Ups Section 🔒",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "To access settings, please answer: \nWhat is $num1 + $num2 ?",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    options.forEach { opt ->
                        PupBouncyButton(
                            text = "$opt",
                            onClick = {
                                if (opt == answer) {
                                    PupSoundEffects.playSuccessChime()
                                    onConfirmed()
                                } else {
                                    PupSoundEffects.playBoing()
                                    hasError = true
                                }
                            },
                            color = PawSunYellow,
                            textColor = PawDarkText,
                            fontSize = 16,
                            modifier = Modifier.width(70.dp),
                            testTag = "math_option_$opt"
                        )
                    }
                }
                if (hasError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try again!",
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
