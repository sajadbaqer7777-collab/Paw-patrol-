package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PupSoundEffects
import com.example.model.PupCharacter
import com.example.ui.components.PupBouncyButton
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawFireOrange
import com.example.ui.theme.PawPink
import com.example.ui.theme.PawSky
import com.example.ui.theme.PawSunYellow

data class PhonicsQuestion(
    val targetLetter: Char,
    val wordExample: String,
    val emoji: String,
    val choices: List<Char>
)

val phonicsQuestions = listOf(
    PhonicsQuestion('B', "Bone", "🦴", listOf('B', 'P', 'D', 'M')),
    PhonicsQuestion('S', "Skye", "✈️", listOf('S', 'C', 'Z', 'A')),
    PhonicsQuestion('P', "Paw", "🐾", listOf('P', 'B', 'R', 'T')),
    PhonicsQuestion('C', "Chase", "⭐", listOf('C', 'O', 'G', 'K')),
    PhonicsQuestion('M', "Marshall", "🚒", listOf('M', 'W', 'N', 'H')),
    PhonicsQuestion('R', "Rescue", "🛟", listOf('R', 'P', 'B', 'L')),
    PhonicsQuestion('A', "Apple", "🍎", listOf('A', 'E', 'V', 'M')),
    PhonicsQuestion('F', "Fire", "🔥", listOf('F', 'E', 'T', 'H')),
    PhonicsQuestion('H', "Helicopter", "🚁", listOf('H', 'N', 'M', 'U')),
    PhonicsQuestion('Z', "Zuma", "🌊", listOf('Z', 'S', 'N', 'E'))
)

val balloonColors = listOf(
    Color(0xFFFF5252),
    Color(0xFF448AFF),
    Color(0xFFFFD740),
    Color(0xFF69F0AE),
    Color(0xFFFF4081),
    Color(0xFF7C4DFF)
)

@Composable
fun SkyPhonicsGame(
    onBack: () -> Unit,
    onComplete: (stars: Int, treats: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var currentRound by remember { mutableIntStateOf(0) }
    val totalRounds = 5
    val questions = remember { phonicsQuestions.shuffled().take(totalRounds) }
    val currentQuestion = questions.getOrNull(currentRound) ?: questions.first()

    var feedbackText by remember { mutableStateOf("") }
    var poppedLetter by remember { mutableStateOf<Char?>(null) }

    val floatAnim = remember { Animatable(0f) }
    LaunchedEffect(currentRound) {
        floatAnim.animateTo(
            targetValue = -12f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF81D4FA), // Sky blue
                        Color(0xFFE1F5FE)
                    )
                )
            )
            .testTag("sky_phonics_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Navigation & Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        PupSoundEffects.playPop()
                        onBack()
                    },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .testTag("sky_phonics_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PawDarkText
                    )
                }

                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Round ${currentRound + 1} of $totalRounds",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PawDarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (currentRound + 1).toFloat() / totalRounds },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = PawPink,
                        trackColor = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PawSunYellow
                ) {
                    Text(
                        text = "⭐ +3 Stars",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Skye Speech Prompt Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = PupCharacter.SKYE.imageResId),
                        contentDescription = "Skye",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Skye says:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PawPink
                        )
                        Text(
                            text = "Pop the letter '${currentQuestion.targetLetter}'!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = PawDarkText
                        )
                        Text(
                            text = "${currentQuestion.targetLetter} is for ${currentQuestion.wordExample} ${currentQuestion.emoji}",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            // Floating Balloons Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    currentQuestion.choices.take(2).forEachIndexed { index, letter ->
                        BalloonItem(
                            letter = letter,
                            color = balloonColors[index % balloonColors.size],
                            yOffset = floatAnim.value + (if (index % 2 == 0) -8f else 8f),
                            isPopped = poppedLetter == letter,
                            onClick = {
                                if (letter == currentQuestion.targetLetter) {
                                    PupSoundEffects.playBalloonPop()
                                    PupSoundEffects.playSuccessChime()
                                    poppedLetter = letter
                                    feedbackText = "Pawsome! ${currentQuestion.targetLetter} is for ${currentQuestion.wordExample}! 🌟"
                                    if (currentRound + 1 >= totalRounds) {
                                        onComplete(3, 2)
                                    } else {
                                        currentRound++
                                        poppedLetter = null
                                    }
                                } else {
                                    PupSoundEffects.playBoing()
                                    feedbackText = "That's $letter! Let's find ${currentQuestion.targetLetter}!"
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    currentQuestion.choices.drop(2).forEachIndexed { index, letter ->
                        BalloonItem(
                            letter = letter,
                            color = balloonColors[(index + 2) % balloonColors.size],
                            yOffset = -floatAnim.value + (if (index % 2 == 0) 6f else -6f),
                            isPopped = poppedLetter == letter,
                            onClick = {
                                if (letter == currentQuestion.targetLetter) {
                                    PupSoundEffects.playBalloonPop()
                                    PupSoundEffects.playSuccessChime()
                                    poppedLetter = letter
                                    feedbackText = "Great job! ${currentQuestion.targetLetter} is for ${currentQuestion.wordExample}! 🌟"
                                    if (currentRound + 1 >= totalRounds) {
                                        onComplete(3, 2)
                                    } else {
                                        currentRound++
                                        poppedLetter = null
                                    }
                                } else {
                                    PupSoundEffects.playBoing()
                                    feedbackText = "That's $letter! Let's find ${currentQuestion.targetLetter}!"
                                }
                            }
                        )
                    }
                }
            }

            // Feedback Banner
            if (feedbackText.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(2.dp, PawSunYellow),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = feedbackText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PawDarkText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BalloonItem(
    letter: Char,
    color: Color,
    yOffset: Float,
    isPopped: Boolean,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(0, yOffset.dp.roundToPx()) }
            .scale(if (isPopped) 0.1f else scale.value)
            .size(110.dp)
            .clickable { onClick() }
            .testTag("balloon_$letter"),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Balloon body
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                color.copy(alpha = 0.95f),
                                color
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Highlighting glare
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .offset((-22).dp, (-22).dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.45f))
                )
                Text(
                    text = "$letter",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
            // Balloon knot & string
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(14.dp)
                    .background(Color.White.copy(alpha = 0.8f))
            )
        }
    }
}
