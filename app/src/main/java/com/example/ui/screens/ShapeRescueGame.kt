package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.PawBlue
import com.example.ui.theme.PawCardBorder
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawSunYellow

data class RescueShape(
    val id: String,
    val name: String,
    val symbol: String,
    val color: Color,
    val description: String
)

val puzzleShapes = listOf(
    RescueShape("star", "Star Badge", "⭐", Color(0xFFFFD54F), "Five shining points!"),
    RescueShape("circle", "Rescue Ring", "⭕", Color(0xFF42A5F5), "Round like a steering wheel!"),
    RescueShape("heart", "Pup Heart", "❤️", Color(0xFFEF5350), "Full of pup love!"),
    RescueShape("triangle", "Warning Cone", "🔺", Color(0xFFFFA726), "Three strong corners!"),
    RescueShape("square", "Tool Box", "🟦", Color(0xFF26A69A), "Four equal sides!"),
    RescueShape("diamond", "Crystal Gem", "💎", Color(0xFFAB47BC), "Sparkling rescue gem!")
)

@Composable
fun ShapeRescueGame(
    onBack: () -> Unit,
    onComplete: (stars: Int, treats: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val matchedShapes = remember { mutableStateMapOf<String, Boolean>() }
    var selectedShapeId by remember { mutableStateOf<String?>(null) }
    var hintMessage by remember { mutableStateOf("Tap a shape below, then tap its matching silhouette!") }

    val totalShapes = puzzleShapes.size
    val matchedCount = matchedShapes.values.count { it }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFBBDEFB), // Soft police blue
                        Color(0xFFE3F2FD)
                    )
                )
            )
            .testTag("shape_rescue_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                        .testTag("shape_back_button")
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
                        text = "Matched: $matchedCount of $totalShapes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PawDarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { matchedCount.toFloat() / totalShapes },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = PawBlue,
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

            // Chase Prompt Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = PupCharacter.CHASE.imageResId),
                        contentDescription = "Chase",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Chase is on the case!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PawBlue
                        )
                        Text(
                            text = "Shape Detective Mission",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = PawDarkText
                        )
                        Text(
                            text = hintMessage,
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            // Silhouette Board (3x2 grid of slots)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.9f),
                border = BorderStroke(2.dp, PawCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 10.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(puzzleShapes) { shape ->
                        val isMatched = matchedShapes[shape.id] == true
                        val isSelected = selectedShapeId == shape.id

                        Box(
                            modifier = Modifier
                                .height(95.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (isMatched) shape.color.copy(alpha = 0.35f)
                                    else Color(0xFFECEFF1)
                                )
                                .border(
                                    BorderStroke(
                                        width = if (isSelected) 3.dp else 1.5.dp,
                                        color = if (isSelected) PawBlue
                                        else if (isMatched) shape.color
                                        else Color.LightGray
                                    ),
                                    RoundedCornerShape(18.dp)
                                )
                                .clickable {
                                    if (!isMatched) {
                                        if (selectedShapeId == shape.id) {
                                            // Matched!
                                            PupSoundEffects.playSuccessChime()
                                            PupSoundEffects.playPop()
                                            matchedShapes[shape.id] = true
                                            hintMessage = "Match! ${shape.name}: ${shape.description} ⭐"
                                            selectedShapeId = null

                                            if (matchedShapes.size == totalShapes) {
                                                PupSoundEffects.playFanfare()
                                                onComplete(3, 2)
                                            }
                                        } else if (selectedShapeId != null) {
                                            PupSoundEffects.playBoing()
                                            hintMessage = "Not quite! Look closely at the shapes!"
                                        } else {
                                            selectedShapeId = shape.id
                                            hintMessage = "Now find the ${shape.name} token below!"
                                        }
                                    }
                                }
                                .testTag("slot_${shape.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = if (isMatched) shape.symbol else "❓",
                                    fontSize = if (isMatched) 38.sp else 28.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = shape.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMatched) PawDarkText else Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Shape Tokens Tray
            Text(
                text = "Shape Tokens (Tap to match)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PawDarkText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                puzzleShapes.forEach { shape ->
                    val isMatched = matchedShapes[shape.id] == true
                    val isSelected = selectedShapeId == shape.id

                    Surface(
                        shape = CircleShape,
                        color = if (isMatched) Color.LightGray else shape.color,
                        border = if (isSelected) BorderStroke(3.dp, PawBlue) else null,
                        shadowElevation = if (isMatched) 1.dp else 4.dp,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable(enabled = !isMatched) {
                                PupSoundEffects.playPop()
                                selectedShapeId = shape.id
                                hintMessage = "Great! Now tap the slot for ${shape.name} above!"
                            }
                            .testTag("token_${shape.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = shape.symbol,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
