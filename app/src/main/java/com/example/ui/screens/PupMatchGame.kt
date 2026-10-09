package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PupSoundEffects
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawSunYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MemoryCard(
    val id: Int,
    val symbol: String,
    val name: String,
    val cardColor: Color
)

@Composable
fun PupMatchGame(
    onBack: () -> Unit,
    onComplete: (stars: Int, treats: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val coroutineScope = rememberCoroutineScope()

    val initialSymbols = remember {
        listOf(
            Triple("⭐", "Star", Color(0xFFFFD54F)),
            Triple("🦴", "Bone", Color(0xFFFFCC80)),
            Triple("🐾", "Paw", Color(0xFF81D4FA)),
            Triple("🛟", "Lifebuoy", Color(0xFFFF8A65)),
            Triple("🚒", "Engine", Color(0xFFEF9A9A)),
            Triple("👑", "Crown", Color(0xFFCE93D8))
        )
    }

    val cards = remember {
        val pairs = initialSymbols.flatMapIndexed { index, (symbol, name, color) ->
            listOf(
                MemoryCard(index * 2, symbol, name, color),
                MemoryCard(index * 2 + 1, symbol, name, color)
            )
        }.shuffled()
        pairs
    }

    val flippedIndices = remember { mutableStateListOf<Int>() }
    val matchedIds = remember { mutableStateListOf<String>() }
    var turnsCount by remember { mutableIntStateOf(0) }
    var hintText by remember { mutableStateOf("Flip cards to find pairs!") }

    val totalPairs = initialSymbols.size
    val matchedPairsCount = matchedIds.size

    fun onCardClick(index: Int) {
        val card = cards[index]
        if (flippedIndices.contains(index) || matchedIds.contains(card.symbol)) return
        if (flippedIndices.size == 2) return

        PupSoundEffects.playPop()
        flippedIndices.add(index)

        if (flippedIndices.size == 2) {
            turnsCount++
            val firstCard = cards[flippedIndices[0]]
            val secondCard = cards[flippedIndices[1]]

            if (firstCard.symbol == secondCard.symbol) {
                // Match!
                PupSoundEffects.playSuccessChime()
                matchedIds.add(firstCard.symbol)
                hintText = "Pawsome match! Found ${firstCard.name}! 🌟"
                flippedIndices.clear()

                if (matchedIds.size == totalPairs) {
                    PupSoundEffects.playFanfare()
                    coroutineScope.launch {
                        delay(500)
                        onComplete(4, 3)
                    }
                }
            } else {
                PupSoundEffects.playBoing()
                hintText = "Almost! Remember their spots!"
                coroutineScope.launch {
                    delay(900)
                    flippedIndices.clear()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFE0B2), // Warm orange beach
                        Color(0xFFFFF3E0)
                    )
                )
            )
            .testTag("pup_match_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Nav
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
                        .testTag("match_back_button")
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
                        text = "Pairs: $matchedPairsCount / $totalPairs  (Turns: $turnsCount)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PawDarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { matchedPairsCount.toFloat() / totalPairs },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFFFF9800),
                        trackColor = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PawSunYellow
                ) {
                    Text(
                        text = "⭐ +4 Stars",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Message Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = hintText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PawDarkText,
                    modifier = Modifier.padding(10.dp)
                )
            }

            // Cards Grid (3 columns, 4 rows)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(cards) { index, card ->
                    val isFlipped = flippedIndices.contains(index) || matchedIds.contains(card.symbol)
                    val rotation by animateFloatAsState(
                        targetValue = if (isFlipped) 180f else 0f,
                        animationSpec = tween(400),
                        label = "card_flip"
                    )

                    Card(
                        modifier = Modifier
                            .height(105.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .clickable { onCardClick(index) }
                            .testTag("card_$index"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (rotation > 90f) card.cardColor else Color(0xFFFF7043)
                        ),
                        border = BorderStroke(2.dp, Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation > 90f) {
                                // Face Up
                                Text(
                                    text = card.symbol,
                                    fontSize = 42.sp,
                                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                                )
                            } else {
                                // Face Down
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🐾", fontSize = 28.sp)
                                    Text(
                                        text = "PAW",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
