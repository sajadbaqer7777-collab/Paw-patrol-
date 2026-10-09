package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PupSoundEffects
import com.example.model.PupCharacter
import com.example.ui.components.PupBouncyButton
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawFireOrange
import com.example.ui.theme.PawRed
import com.example.ui.theme.PawSunYellow
import kotlinx.coroutines.launch

@Composable
fun BlazeCounterGame(
    onBack: () -> Unit,
    onComplete: (stars: Int, treats: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val coroutineScope = rememberCoroutineScope()
    var currentRound by remember { mutableIntStateOf(0) }
    val totalRounds = 4
    val targetCounts = remember { listOf(3, 4, 5, 6) }
    val currentTarget = targetCounts[currentRound % targetCounts.size]

    var treatsInBowl by remember { mutableIntStateOf(0) }
    var feedbackMessage by remember { mutableStateOf("Tap the bones to drop them into the bowl!") }

    val pupScale = remember { Animatable(1f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFECB3), // Warm amber yellow
                        Color(0xFFFFE0B2)
                    )
                )
            )
            .testTag("blaze_counter_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header bar
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
                        .testTag("blaze_back_button")
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
                        text = "Counting Mission: Round ${currentRound + 1}/$totalRounds",
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
                        color = PawRed,
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

            // Marshall Prompt Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = PupCharacter.MARSHALL.imageResId),
                        contentDescription = "Marshall",
                        modifier = Modifier
                            .size(72.dp)
                            .scale(pupScale.value)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Marshall is hungry!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PawRed
                        )
                        Text(
                            text = "Feed Marshall $currentTarget bone treats!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = PawDarkText
                        )
                        Text(
                            text = "Current: $treatsInBowl / $currentTarget in bowl 🥣",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PawFireOrange
                        )
                    }
                }
            }

            // Center Interactive Doggy Bowl
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFFFFD54F),
                                Color(0xFFFFB300)
                            )
                        )
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🥣", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$treatsInBowl Treats",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color(0xFF3E2723)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        repeat(minOf(treatsInBowl, 8)) {
                            Text(text = "🦴", fontSize = 16.sp)
                        }
                    }
                }
            }

            // Bone Treat Dispenser Bar
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = feedbackMessage,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PawDarkText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tap to Add Bone Button
                    PupBouncyButton(
                        text = "Drop Bone 🦴",
                        onClick = {
                            if (treatsInBowl < currentTarget) {
                                treatsInBowl++
                                PupSoundEffects.playPop()
                                PupSoundEffects.playCrunch()
                                feedbackMessage = "Yum! That makes $treatsInBowl! 🦴"

                                coroutineScope.launch {
                                    pupScale.animateTo(1.15f, tween(100))
                                    pupScale.animateTo(1f, tween(150))
                                }

                                if (treatsInBowl == currentTarget) {
                                    PupSoundEffects.playSuccessChime()
                                    PupSoundEffects.playBark()
                                    feedbackMessage = "Woohoo! Exactly $currentTarget treats! Pawsome! 🎉"
                                    coroutineScope.launch {
                                        kotlinx.coroutines.delay(1200)
                                        if (currentRound + 1 >= totalRounds) {
                                            onComplete(3, 3)
                                        } else {
                                            currentRound++
                                            treatsInBowl = 0
                                            feedbackMessage = "Next round! Marshall needs more treats!"
                                        }
                                    }
                                }
                            }
                        },
                        color = PawRed,
                        fontSize = 16,
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        testTag = "drop_bone_button"
                    )

                    // Empty bowl / Reset round button
                    PupBouncyButton(
                        text = "Clear 🔄",
                        onClick = {
                            PupSoundEffects.playBoing()
                            treatsInBowl = 0
                            feedbackMessage = "Bowl emptied! Count again!"
                        },
                        color = Color(0xFF78909C),
                        fontSize = 14,
                        modifier = Modifier.width(100.dp),
                        testTag = "clear_bowl_button"
                    )
                }
            }
        }
    }
}
