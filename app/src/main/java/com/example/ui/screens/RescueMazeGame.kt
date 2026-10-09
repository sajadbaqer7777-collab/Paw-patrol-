package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PupSoundEffects
import com.example.model.PupCharacter
import com.example.ui.components.PupBouncyButton
import com.example.ui.theme.PawDarkText
import com.example.ui.theme.PawGreen
import com.example.ui.theme.PawSunYellow

data class MazeCell(
    val row: Int,
    val col: Int,
    val isObstacle: Boolean = false,
    val hasSnack: Boolean = false
)

@Composable
fun RescueMazeGame(
    onBack: () -> Unit,
    onComplete: (stars: Int, treats: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val gridSize = 5
    var pupRow by remember { mutableIntStateOf(0) }
    var pupCol by remember { mutableIntStateOf(0) }
    val goalRow = 4
    val goalCol = 4

    val obstacles = remember {
        setOf(
            Pair(1, 1),
            Pair(1, 3),
            Pair(3, 1),
            Pair(2, 3)
        )
    }

    val snacks = remember {
        mutableStateListOf(
            Pair(0, 3),
            Pair(2, 0),
            Pair(3, 4)
        )
    }

    var snacksCollected by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("Guide the rescue pup to save the lost kitten!") }

    fun movePup(deltaRow: Int, deltaCol: Int) {
        val newRow = pupRow + deltaRow
        val newCol = pupCol + deltaCol

        if (newRow in 0 until gridSize && newCol in 0 until gridSize) {
            if (Pair(newRow, newCol) in obstacles) {
                PupSoundEffects.playBoing()
                message = "Oops! Road blocked by cones or trees! Try another way!"
            } else {
                pupRow = newRow
                pupCol = newCol
                PupSoundEffects.playPop()

                // Check snack
                if (Pair(newRow, newCol) in snacks) {
                    snacks.remove(Pair(newRow, newCol))
                    snacksCollected++
                    PupSoundEffects.playCrunch()
                    message = "Yum! Bone treat collected! 🦴"
                }

                // Check goal
                if (newRow == goalRow && newCol == goalCol) {
                    PupSoundEffects.playBark()
                    PupSoundEffects.playFanfare()
                    message = "Hurray! You saved the kitten! Pawsome rescue! 🐱🎉"
                    onComplete(4, 2 + snacksCollected)
                }
            }
        } else {
            PupSoundEffects.playBoing()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFC8E6C9), // Friendly green
                        Color(0xFFE8F5E9)
                    )
                )
            )
            .testTag("rescue_maze_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
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
                        .testTag("maze_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PawDarkText
                    )
                }

                Text(
                    text = "Rescue Route Maze",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = PawDarkText
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PawSunYellow
                ) {
                    Text(
                        text = "🦴 Snacks: $snacksCollected",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Message pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PawDarkText,
                    modifier = Modifier.padding(10.dp)
                )
            }

            // Maze Grid
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF1F8E9),
                border = BorderStroke(2.dp, Color(0xFF81C784)),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (r in 0 until gridSize) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            for (c in 0 until gridSize) {
                                val isPup = r == pupRow && c == pupCol
                                val isGoal = r == goalRow && c == goalCol
                                val isObstacle = Pair(r, c) in obstacles
                                val hasSnack = Pair(r, c) in snacks

                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when {
                                                isPup -> Color(0xFF64B5F6)
                                                isGoal -> Color(0xFFFFCC80)
                                                isObstacle -> Color(0xFFB0BEC5)
                                                else -> Color.White
                                            }
                                        )
                                        .testTag("cell_${r}_$c"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when {
                                        isPup -> Text("🐶", fontSize = 26.sp)
                                        isGoal -> Text("🐱", fontSize = 26.sp)
                                        isObstacle -> Text("🚧", fontSize = 22.sp)
                                        hasSnack -> Text("🦴", fontSize = 20.sp)
                                        else -> Text("·", fontSize = 16.sp, color = Color.LightGray)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Direction Arrow Keypad for Kids
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                // UP
                IconButton(
                    onClick = { movePup(-1, 0) },
                    modifier = Modifier
                        .size(52.dp)
                        .background(PawGreen, CircleShape)
                        .testTag("maze_up_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Up",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT
                    IconButton(
                        onClick = { movePup(0, -1) },
                        modifier = Modifier
                            .size(52.dp)
                            .background(PawGreen, CircleShape)
                            .testTag("maze_left_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Left",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // DOWN
                    IconButton(
                        onClick = { movePup(1, 0) },
                        modifier = Modifier
                            .size(52.dp)
                            .background(PawGreen, CircleShape)
                            .testTag("maze_down_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Down",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // RIGHT
                    IconButton(
                        onClick = { movePup(0, 1) },
                        modifier = Modifier
                            .size(52.dp)
                            .background(PawGreen, CircleShape)
                            .testTag("maze_right_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Right",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}
