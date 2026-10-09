package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R

enum class PupCharacter(
    val pupName: String,
    val roleTitle: String,
    val themeColor: Color,
    val darkColor: Color,
    val catchphrase: String,
    val imageResId: Int,
    val badgeIconName: String
) {
    CHASE(
        pupName = "Chase",
        roleTitle = "Police & Traffic Pup",
        themeColor = Color(0xFF1E88E5), // Blue
        darkColor = Color(0xFF0D47A1),
        catchphrase = "Chase is on the case!",
        imageResId = R.drawable.img_pup_chase,
        badgeIconName = "Star Badge"
    ),
    SKYE(
        pupName = "Skye",
        roleTitle = "Aviation & Sky Pup",
        themeColor = Color(0xFFEC407A), // Pink
        darkColor = Color(0xFFC2185B),
        catchphrase = "Let's take to the sky!",
        imageResId = R.drawable.img_pup_skye,
        badgeIconName = "Pilot Wings"
    ),
    MARSHALL(
        pupName = "Marshall",
        roleTitle = "Fire & Rescue Pup",
        themeColor = Color(0xFFE53935), // Red
        darkColor = Color(0xFFB71C1C),
        catchphrase = "I'm fired up and ready to roll!",
        imageResId = R.drawable.img_pup_marshall,
        badgeIconName = "Fire Shield"
    ),
    RUBBLE(
        pupName = "Rubble",
        roleTitle = "Construction & Builder Pup",
        themeColor = Color(0xFFFDD835), // Yellow
        darkColor = Color(0xFFF57F17),
        catchphrase = "Rubble on the double!",
        imageResId = R.drawable.ic_launcher_paw,
        badgeIconName = "Wrench & Gear"
    ),
    ZUMA(
        pupName = "Zuma",
        roleTitle = "Water Rescue Pup",
        themeColor = Color(0xFFFF9800), // Orange
        darkColor = Color(0xFFE65100),
        catchphrase = "Ready, set, get wet!",
        imageResId = R.drawable.ic_launcher_paw,
        badgeIconName = "Lifebuoy & Wave"
    )
}

enum class GameType(
    val title: String,
    val subtitle: String,
    val hostPup: PupCharacter,
    val accentColor: Color,
    val emoji: String,
    val recommendedAge: String
) {
    SKY_PHONICS(
        title = "Sky Phonics",
        subtitle = "Pop letter balloons with Skye!",
        hostPup = PupCharacter.SKYE,
        accentColor = Color(0xFFEC407A),
        emoji = "🎈",
        recommendedAge = "Ages 3-6"
    ),
    BLAZE_COUNTER(
        title = "Blaze's Treat Counter",
        subtitle = "Count & feed crunchy treats!",
        hostPup = PupCharacter.MARSHALL,
        accentColor = Color(0xFFE53935),
        emoji = "🦴",
        recommendedAge = "Ages 3-6"
    ),
    SHAPE_RESCUE(
        title = "Shape Detective",
        subtitle = "Match rescue shapes & badges!",
        hostPup = PupCharacter.CHASE,
        accentColor = Color(0xFF1E88E5),
        emoji = "🔷",
        recommendedAge = "Ages 2-5"
    ),
    RESCUE_MAZE(
        title = "Rescue Route Maze",
        subtitle = "Guide the pup to save the kitten!",
        hostPup = PupCharacter.CHASE,
        accentColor = Color(0xFF43A047),
        emoji = "🚒",
        recommendedAge = "Ages 4-7"
    ),
    PUP_MATCH(
        title = "Pup Memory Match",
        subtitle = "Flip & find the matching pairs!",
        hostPup = PupCharacter.ZUMA,
        accentColor = Color(0xFFFF9800),
        emoji = "🎴",
        recommendedAge = "Ages 3-7"
    ),
    STICKER_STUDIO(
        title = "Sticker & Art Studio",
        subtitle = "Create badge masterpieces & draw!",
        hostPup = PupCharacter.RUBBLE,
        accentColor = Color(0xFF8E24AA),
        emoji = "🎨",
        recommendedAge = "All Ages"
    )
}

data class PupBadge(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val starsRequired: Int,
    val isUnlocked: Boolean = false
)

enum class ScreenState {
    PORTAL_HOME,
    GAME_SKY_PHONICS,
    GAME_BLAZE_COUNTER,
    GAME_SHAPE_RESCUE,
    GAME_RESCUE_MAZE,
    GAME_PUP_MATCH,
    GAME_STICKER_STUDIO,
    TROPHY_ROOM,
    PARENTAL_SETTINGS
}
