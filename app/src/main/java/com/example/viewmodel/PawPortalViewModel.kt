package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.PupSoundEffects
import com.example.model.GameType
import com.example.model.PupBadge
import com.example.model.PupCharacter
import com.example.model.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PawPortalUiState(
    val currentScreen: ScreenState = ScreenState.PORTAL_HOME,
    val selectedCompanion: PupCharacter = PupCharacter.CHASE,
    val cadetName: String = "Cadet Sparky",
    val starsCount: Int = 12,
    val treatsCount: Int = 5,
    val isSoundEnabled: Boolean = true,
    val badges: List<PupBadge> = initialBadges,
    val isCelebrationOpen: Boolean = false,
    val celebrationTitle: String = "Mission Complete!",
    val celebrationSubtitle: String = "You earned 3 Pup Stars!",
    val lastFedPupMessage: String = "",
    val showCompanionPicker: Boolean = false,
    val showParentalSettings: Boolean = false,
    val gamesPlayedCount: Int = 0
)

val initialBadges = listOf(
    PupBadge("b_first_rescue", "First Rescue", "Completed your very first mission!", "🏅", 1, true),
    PupBadge("b_alphabet_ace", "Sky Alphabet Ace", "Mastered letter balloons with Skye!", "🎈", 15, false),
    PupBadge("b_treat_counter", "Super Counter", "Fed and counted snacks with Marshall!", "🦴", 20, false),
    PupBadge("b_shape_detective", "Shape Detective", "Matched all rescue silhouettes with Chase!", "🔷", 25, false),
    PupBadge("b_maze_runner", "Rescue Path Finder", "Navigated through Adventure Bay to save pets!", "🚒", 30, false),
    PupBadge("b_memory_master", "Pup Memory Whiz", "Found all matching cards with Zuma!", "🎴", 35, false),
    PupBadge("b_pup_artist", "Creative Cadet", "Created a sticker badge masterpiece!", "🎨", 40, false),
    PupBadge("b_golden_bone", "Golden Bone Trophy", "Fed 10 delicious treats to your companions!", "🍖", 50, false),
    PupBadge("b_grand_hero", "Ultimate Rescue Pup", "Collected 100 Pup Stars across all missions!", "👑", 100, false)
)

class PawPortalViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PawPortalUiState())
    val uiState: StateFlow<PawPortalUiState> = _uiState.asStateFlow()

    init {
        PupSoundEffects.isSoundEnabled = _uiState.value.isSoundEnabled
    }

    fun navigateTo(screen: ScreenState) {
        PupSoundEffects.playPop()
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectCompanion(pup: PupCharacter) {
        PupSoundEffects.playBark()
        _uiState.update {
            it.copy(
                selectedCompanion = pup,
                showCompanionPicker = false,
                lastFedPupMessage = "${pup.pupName}: \"${pup.catchphrase}\""
            )
        }
    }

    fun toggleCompanionPicker(show: Boolean) {
        PupSoundEffects.playPop()
        _uiState.update { it.copy(showCompanionPicker = show) }
    }

    fun toggleSound(enabled: Boolean) {
        PupSoundEffects.isSoundEnabled = enabled
        _uiState.update { it.copy(isSoundEnabled = enabled) }
        if (enabled) {
            PupSoundEffects.playSuccessChime()
        }
    }

    fun feedCompanion() {
        val current = _uiState.value
        if (current.treatsCount <= 0) {
            PupSoundEffects.playBoing()
            _uiState.update {
                it.copy(lastFedPupMessage = "No treats left! Play a mini-game to earn more bones! 🦴")
            }
            return
        }

        PupSoundEffects.playCrunch()
        PupSoundEffects.playBark()

        val reactions = listOf(
            "${current.selectedCompanion.pupName} crunches the bone and wags happily! 🦴✨",
            "${current.selectedCompanion.pupName} does a joyful backflip! \"Yum yum yum!\"",
            "\"Thank you, Cadet! That was delicious!\" *Happy tail wags*",
            "${current.selectedCompanion.pupName} barks cheerfully: \"${current.selectedCompanion.catchphrase}\""
        )
        val reaction = reactions.random()

        _uiState.update { state ->
            val updatedTreats = state.treatsCount - 1
            state.copy(
                treatsCount = updatedTreats,
                lastFedPupMessage = reaction
            )
        }
    }

    fun completeMission(title: String, starsEarned: Int, treatsEarned: Int) {
        PupSoundEffects.playFanfare()
        _uiState.update { state ->
            val newStars = state.starsCount + starsEarned
            val newTreats = state.treatsCount + treatsEarned
            val newGamesCount = state.gamesPlayedCount + 1

            // Check badge unlock thresholds
            val updatedBadges = state.badges.map { badge ->
                if (!badge.isUnlocked && newStars >= badge.starsRequired) {
                    badge.copy(isUnlocked = true)
                } else {
                    badge
                }
            }

            state.copy(
                starsCount = newStars,
                treatsCount = newTreats,
                badges = updatedBadges,
                gamesPlayedCount = newGamesCount,
                isCelebrationOpen = true,
                celebrationTitle = title,
                celebrationSubtitle = "Pawsome job! You earned +$starsEarned Stars & +$treatsEarned Bones! 🌟🦴"
            )
        }
    }

    fun dismissCelebration() {
        PupSoundEffects.playPop()
        _uiState.update { it.copy(isCelebrationOpen = false) }
    }

    fun setParentalSettingsVisible(visible: Boolean) {
        _uiState.update { it.copy(showParentalSettings = visible) }
    }

    fun updateCadetName(name: String) {
        _uiState.update { it.copy(cadetName = name.ifBlank { "Cadet Sparky" }) }
    }

    fun resetAllProgress() {
        PupSoundEffects.playPop()
        _uiState.update {
            PawPortalUiState(
                starsCount = 5,
                treatsCount = 3,
                badges = initialBadges,
                showParentalSettings = false
            )
        }
    }
}
