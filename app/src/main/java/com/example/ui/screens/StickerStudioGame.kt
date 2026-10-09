package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PupSoundEffects
import com.example.ui.components.PupBouncyButton
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawPurple
import com.example.ui.theme.PawSunYellow

data class DrawingPath(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float = 14f
)

data class PlacedSticker(
    val id: Int,
    val symbol: String,
    val offset: Offset
)

val studioPalette = listOf(
    Color(0xFFE53935), // Red
    Color(0xFF1E88E5), // Blue
    Color(0xFFFDD835), // Yellow
    Color(0xFF43A047), // Green
    Color(0xFFEC407A), // Pink
    Color(0xFF8E24AA), // Purple
    Color(0xFFFF9800)  // Orange
)

val stampStickers = listOf(
    "⭐", "🦴", "🐾", "🚒", "✈️", "🛟", "👑", "❤️", "💎", "🏆"
)

@Composable
fun StickerStudioGame(
    onBack: () -> Unit,
    onComplete: (stars: Int, treats: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val paths = remember { mutableStateListOf<DrawingPath>() }
    var currentPathPoints by remember { mutableStateOf(listOf<Offset>()) }
    var selectedColor by remember { mutableStateOf(studioPalette[0]) }

    val placedStickers = remember { mutableStateListOf<PlacedSticker>() }
    var selectedStickerEmoji by remember { mutableStateOf(stampStickers[0]) }
    var studioMode by remember { mutableStateOf("DRAW") } // "DRAW" or "STAMP"
    var snapshotMessage by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
            .testTag("sticker_studio_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
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
                        .testTag("studio_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PawDarkText
                    )
                }

                Text(
                    text = "Pup Badge & Art Studio 🎨",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = PawDarkText
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Clear Canvas
                    IconButton(
                        onClick = {
                            PupSoundEffects.playBoing()
                            paths.clear()
                            placedStickers.clear()
                            snapshotMessage = "Canvas cleaned!"
                        },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .testTag("studio_clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            tint = Color.Red
                        )
                    }

                    // Save Artwork / Mission Complete
                    IconButton(
                        onClick = {
                            PupSoundEffects.playSuccessChime()
                            PupSoundEffects.playBark()
                            snapshotMessage = "📸 Snapshot saved to Pup Scrapbook! +4 Stars!"
                            onComplete(4, 2)
                        },
                        modifier = Modifier
                            .background(PawSunYellow, CircleShape)
                            .testTag("studio_save_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Save Artwork",
                            tint = PawDarkText
                        )
                    }
                }
            }

            // Drawing & Stamping Canvas
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                border = BorderStroke(2.dp, PawPurple.copy(alpha = 0.3f)),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
                    .pointerInput(studioMode, selectedStickerEmoji, selectedColor) {
                        if (studioMode == "DRAW") {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPathPoints = listOf(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPathPoints = currentPathPoints + change.position
                                },
                                onDragEnd = {
                                    if (currentPathPoints.isNotEmpty()) {
                                        paths.add(DrawingPath(currentPathPoints, selectedColor))
                                        currentPathPoints = emptyList()
                                        PupSoundEffects.playPop()
                                    }
                                }
                            )
                        } else {
                            // Tap to stamp sticker
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val down = event.changes.firstOrNull { it.pressed }
                                    if (down != null) {
                                        down.consume()
                                        placedStickers.add(
                                            PlacedSticker(
                                                id = placedStickers.size,
                                                symbol = selectedStickerEmoji,
                                                offset = down.position
                                            )
                                        )
                                        PupSoundEffects.playPop()
                                        break
                                    }
                                }
                            }
                        }
                    }
                    .testTag("studio_canvas")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Render past paths
                        for (path in paths) {
                            if (path.points.size > 1) {
                                for (i in 0 until path.points.size - 1) {
                                    drawLine(
                                        color = path.color,
                                        start = path.points[i],
                                        end = path.points[i + 1],
                                        strokeWidth = path.strokeWidth,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }
                        // Render current active stroke
                        if (currentPathPoints.size > 1) {
                            for (i in 0 until currentPathPoints.size - 1) {
                                drawLine(
                                    color = selectedColor,
                                    start = currentPathPoints[i],
                                    end = currentPathPoints[i + 1],
                                    strokeWidth = 14f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // Render placed stickers
                    placedStickers.forEach { sticker ->
                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        (sticker.offset.x - 24.dp.roundToPx()).toInt(),
                                        (sticker.offset.y - 24.dp.roundToPx()).toInt()
                                    )
                                }
                        ) {
                            Text(text = sticker.symbol, fontSize = 36.sp)
                        }
                    }

                    if (snapshotMessage.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PawPurple,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = snapshotMessage,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Mode Selector: DRAW vs STAMP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (studioMode == "DRAW") PawPurple else Color.Transparent)
                                .clickable {
                                    PupSoundEffects.playPop()
                                    studioMode = "DRAW"
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("tool_draw_mode")
                        ) {
                            Text(
                                text = "✏️ Draw Pen",
                                color = if (studioMode == "DRAW") Color.White else PawDarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (studioMode == "STAMP") PawPurple else Color.Transparent)
                                .clickable {
                                    PupSoundEffects.playPop()
                                    studioMode = "STAMP"
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("tool_stamp_mode")
                        ) {
                            Text(
                                text = "⭐ Stickers",
                                color = if (studioMode == "STAMP") Color.White else PawDarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Palette / Sticker Tray
            if (studioMode == "DRAW") {
                // Color choices
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    studioPalette.forEach { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    BorderStroke(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.Black else Color.LightGray
                                    ),
                                    CircleShape
                                )
                                .clickable {
                                    PupSoundEffects.playPop()
                                    selectedColor = color
                                }
                                .testTag("color_button")
                        )
                    }
                }
            } else {
                // Sticker choices
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    stampStickers.take(7).forEach { emoji ->
                        val isSelected = selectedStickerEmoji == emoji
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) PawSunYellow else Color.White,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    PupSoundEffects.playPop()
                                    selectedStickerEmoji = emoji
                                }
                                .testTag("stamp_$emoji")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = emoji, fontSize = 22.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
