package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ScreenState
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.CompanionSelectorDialog
import com.example.ui.components.ParentalGateDialog
import com.example.ui.components.ParentalSettingsDialog
import com.example.ui.components.PupTopAppBar
import com.example.ui.screens.BlazeCounterGame
import com.example.ui.screens.PortalHomeScreen
import com.example.ui.screens.PupMatchGame
import com.example.ui.screens.RescueMazeGame
import com.example.ui.screens.ShapeRescueGame
import com.example.ui.screens.SkyPhonicsGame
import com.example.ui.screens.StickerStudioGame
import com.example.ui.screens.TrophyRoomScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.PawPortalViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PawPortalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PawPortalApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PawPortalApp(viewModel: PawPortalViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showParentalGate by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.currentScreen == ScreenState.PORTAL_HOME) {
                PupTopAppBar(
                    cadetName = uiState.cadetName,
                    starsCount = uiState.starsCount,
                    treatsCount = uiState.treatsCount,
                    currentCompanion = uiState.selectedCompanion,
                    isSoundEnabled = uiState.isSoundEnabled,
                    onCompanionClick = { viewModel.toggleCompanionPicker(true) },
                    onTrophyClick = { viewModel.navigateTo(ScreenState.TROPHY_ROOM) },
                    onSoundToggle = { viewModel.toggleSound(it) },
                    onParentalSettingsClick = { showParentalGate = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                ScreenState.PORTAL_HOME -> {
                    PortalHomeScreen(
                        selectedCompanion = uiState.selectedCompanion,
                        starsCount = uiState.starsCount,
                        treatsCount = uiState.treatsCount,
                        lastFedMessage = uiState.lastFedPupMessage,
                        onNavigateGame = { screen -> viewModel.navigateTo(screen) },
                        onFeedPup = { viewModel.feedCompanion() },
                        onChangeCompanion = { viewModel.toggleCompanionPicker(true) }
                    )
                }

                ScreenState.GAME_SKY_PHONICS -> {
                    SkyPhonicsGame(
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) },
                        onComplete = { stars, treats ->
                            viewModel.completeMission("Sky Phonics Cleared!", stars, treats)
                            viewModel.navigateTo(ScreenState.PORTAL_HOME)
                        }
                    )
                }

                ScreenState.GAME_BLAZE_COUNTER -> {
                    BlazeCounterGame(
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) },
                        onComplete = { stars, treats ->
                            viewModel.completeMission("Treat Counter Cleared!", stars, treats)
                            viewModel.navigateTo(ScreenState.PORTAL_HOME)
                        }
                    )
                }

                ScreenState.GAME_SHAPE_RESCUE -> {
                    ShapeRescueGame(
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) },
                        onComplete = { stars, treats ->
                            viewModel.completeMission("Shape Detective Cleared!", stars, treats)
                            viewModel.navigateTo(ScreenState.PORTAL_HOME)
                        }
                    )
                }

                ScreenState.GAME_RESCUE_MAZE -> {
                    RescueMazeGame(
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) },
                        onComplete = { stars, treats ->
                            viewModel.completeMission("Rescue Maze Navigated!", stars, treats)
                            viewModel.navigateTo(ScreenState.PORTAL_HOME)
                        }
                    )
                }

                ScreenState.GAME_PUP_MATCH -> {
                    PupMatchGame(
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) },
                        onComplete = { stars, treats ->
                            viewModel.completeMission("Memory Pairs Matched!", stars, treats)
                            viewModel.navigateTo(ScreenState.PORTAL_HOME)
                        }
                    )
                }

                ScreenState.GAME_STICKER_STUDIO -> {
                    StickerStudioGame(
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) },
                        onComplete = { stars, treats ->
                            viewModel.completeMission("Badge Artwork Created!", stars, treats)
                            viewModel.navigateTo(ScreenState.PORTAL_HOME)
                        }
                    )
                }

                ScreenState.TROPHY_ROOM -> {
                    TrophyRoomScreen(
                        badges = uiState.badges,
                        starsCount = uiState.starsCount,
                        treatsCount = uiState.treatsCount,
                        gamesPlayed = uiState.gamesPlayedCount,
                        cadetName = uiState.cadetName,
                        companion = uiState.selectedCompanion,
                        onBack = { viewModel.navigateTo(ScreenState.PORTAL_HOME) }
                    )
                }

                ScreenState.PARENTAL_SETTINGS -> {
                    // Handled via Dialog
                }
            }

            // Celebration Modal
            CelebrationDialog(
                isOpen = uiState.isCelebrationOpen,
                title = uiState.celebrationTitle,
                subtitle = uiState.celebrationSubtitle,
                companion = uiState.selectedCompanion,
                onDismiss = { viewModel.dismissCelebration() }
            )

            // Companion Picker Modal
            CompanionSelectorDialog(
                isOpen = uiState.showCompanionPicker,
                currentCompanion = uiState.selectedCompanion,
                onCompanionSelected = { pup -> viewModel.selectCompanion(pup) },
                onDismiss = { viewModel.toggleCompanionPicker(false) }
            )

            // Parental Gate Challenge Dialog
            ParentalGateDialog(
                isOpen = showParentalGate,
                onConfirmed = {
                    showParentalGate = false
                    viewModel.setParentalSettingsVisible(true)
                },
                onDismiss = { showParentalGate = false }
            )

            // Parental Settings Dialog
            ParentalSettingsDialog(
                isOpen = uiState.showParentalSettings,
                cadetName = uiState.cadetName,
                isSoundEnabled = uiState.isSoundEnabled,
                onSaveName = { name -> viewModel.updateCadetName(name) },
                onToggleSound = { enabled -> viewModel.toggleSound(enabled) },
                onResetProgress = { viewModel.resetAllProgress() },
                onDismiss = { viewModel.setParentalSettingsVisible(false) }
            )
        }
    }
}
