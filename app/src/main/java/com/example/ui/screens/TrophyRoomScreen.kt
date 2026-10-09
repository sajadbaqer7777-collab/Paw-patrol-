package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.PupBadge
import com.example.model.PupCharacter
import com.example.ui.components.PupBouncyButton
import com.example.ui.theme.PawCardBorder
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawFireOrange
import com.example.ui.theme.PawSunYellow

@Composable
fun TrophyRoomScreen(
    badges: List<PupBadge>,
    starsCount: Int,
    treatsCount: Int,
    gamesPlayed: Int,
    cadetName: String,
    companion: PupCharacter,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val unlockedCount = badges.count { it.isUnlocked }

    val rankTitle = when {
        starsCount >= 60 -> "Ultimate Rescue Pup 👑"
        starsCount >= 30 -> "Rescue Master 🏆"
        starsCount >= 15 -> "Paw Patrol Explorer 🌟"
        else -> "Rookie Cadet 🎖️"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF9C4), // Golden yellow
                        Color(0xFFFFFDE7)
                    )
                )
            )
            .testTag("trophy_room_screen")
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header bar
            item(span = { GridItemSpan(2) }) {
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
                            .testTag("trophy_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PawDarkText
                        )
                    }

                    Text(
                        text = "Pup Trophy Room 🏆",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = PawDarkText
                    )

                    Spacer(modifier = Modifier.width(40.dp))
                }
            }

            // Cadet Hero Badge Card
            item(span = { GridItemSpan(2) }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, PawSunYellow),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = companion.imageResId),
                            contentDescription = companion.pupName,
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = cadetName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = PawDarkText
                            )
                            Text(
                                text = "Rank: $rankTitle",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PawFireOrange
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(text = "⭐ $starsCount Stars", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "🦴 $treatsCount Treats", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "🎮 $gamesPlayed Missions", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Badges Section Header
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Rescue Badges ($unlockedCount / ${badges.size} Unlocked)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PawDarkText,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Badge Items
            items(badges) { badge ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (badge.isUnlocked) Color.White else Color(0xFFF5F5F5)
                    ),
                    border = if (badge.isUnlocked) BorderStroke(2.dp, PawSunYellow) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = if (badge.isUnlocked) 4.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("badge_item_${badge.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (badge.isUnlocked) badge.emoji else "🔒",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = badge.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (badge.isUnlocked) PawDarkText else Color.Gray,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                        Text(
                            text = if (badge.isUnlocked) badge.description else "Need ${badge.starsRequired} ⭐",
                            fontSize = 10.sp,
                            color = if (badge.isUnlocked) Color.DarkGray else PawFireOrange,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
