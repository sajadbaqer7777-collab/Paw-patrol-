package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.PupSoundEffects
import com.example.model.GameType
import com.example.model.PupCharacter
import com.example.model.ScreenState
import com.example.ui.components.PupBouncyButton
import com.example.ui.theme.PawCardBorder
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawFireOrange
import com.example.ui.theme.PawSunYellow

@Composable
fun PortalHomeScreen(
    selectedCompanion: PupCharacter,
    starsCount: Int,
    treatsCount: Int,
    lastFedMessage: String,
    onNavigateGame: (ScreenState) -> Unit,
    onFeedPup: () -> Unit,
    onChangeCompanion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val companionBounce = remember { Animatable(1f) }

    LaunchedEffect(lastFedMessage) {
        if (lastFedMessage.isNotEmpty()) {
            companionBounce.animateTo(1.12f, tween(150))
            companionBounce.animateTo(1f, tween(200))
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .testTag("portal_home_screen"),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Portal Lookout Banner
        item(span = { GridItemSpan(2) }) {
            Card(
                shape = RoundedCornerShape(26.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_portal_banner")
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_paw_portal_hero),
                        contentDescription = "Paw Portal Squad",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay with welcoming text
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.65f)
                                    ),
                                    startY = 60f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "🐾 Paw Portal Lookout",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Choose your mission & earn rescue stars!",
                            color = PawSunYellow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Companion Pup Interaction Card
        item(span = { GridItemSpan(2) }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(2.dp, selectedCompanion.themeColor.copy(alpha = 0.4f)),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("companion_interactive_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pup Image (Tappable for fun barks)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .scale(companionBounce.value)
                            .clip(CircleShape)
                            .background(selectedCompanion.themeColor.copy(alpha = 0.2f))
                            .clickable {
                                PupSoundEffects.playBark()
                            }
                            .testTag("interactive_companion_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = selectedCompanion.imageResId),
                            contentDescription = selectedCompanion.pupName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${selectedCompanion.pupName} Ready!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = selectedCompanion.darkColor
                            )
                            Text(
                                text = "Switch 🔄",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PawFireOrange,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PawSunYellow.copy(alpha = 0.3f))
                                    .clickable { onChangeCompanion() }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = if (lastFedMessage.isNotEmpty()) lastFedMessage else "\"${selectedCompanion.catchphrase}\"",
                            fontSize = 12.sp,
                            color = PawDarkText,
                            modifier = Modifier.padding(vertical = 4.dp),
                            maxLines = 2
                        )

                        // Feed bone treat button
                        PupBouncyButton(
                            text = "Feed Treat 🦴 ($treatsCount)",
                            onClick = onFeedPup,
                            color = PawFireOrange,
                            fontSize = 13,
                            modifier = Modifier.height(38.dp),
                            testTag = "feed_companion_button"
                        )
                    }
                }
            }
        }

        // Mission Section Title
        item(span = { GridItemSpan(2) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "⭐ Active Rescue Missions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PawDarkText
                )
            }
        }

        // Mini Game Mission Cards
        items(GameType.entries) { game ->
            MissionCard(
                game = game,
                onClick = {
                    val targetScreen = when (game) {
                        GameType.SKY_PHONICS -> ScreenState.GAME_SKY_PHONICS
                        GameType.BLAZE_COUNTER -> ScreenState.GAME_BLAZE_COUNTER
                        GameType.SHAPE_RESCUE -> ScreenState.GAME_SHAPE_RESCUE
                        GameType.RESCUE_MAZE -> ScreenState.GAME_RESCUE_MAZE
                        GameType.PUP_MATCH -> ScreenState.GAME_PUP_MATCH
                        GameType.STICKER_STUDIO -> ScreenState.GAME_STICKER_STUDIO
                    }
                    onNavigateGame(targetScreen)
                }
            )
        }
    }
}

@Composable
fun MissionCard(
    game: GameType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {
            PupSoundEffects.playPop()
            onClick()
        },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, game.accentColor.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(185.dp)
            .testTag("mission_card_${game.name.lowercase()}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Emoji & Age Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(game.accentColor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = game.emoji, fontSize = 24.sp)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = game.accentColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = game.recommendedAge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = game.accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Title & Host
            Column {
                Text(
                    text = game.title,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = PawDarkText,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = game.subtitle,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    lineHeight = 14.sp
                )
            }

            // Bottom action strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(game.accentColor)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Play!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
