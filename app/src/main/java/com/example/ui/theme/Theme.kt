package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val PawShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

private val PawColorScheme = lightColorScheme(
    primary = PawBlue,
    onPrimary = Color.White,
    primaryContainer = PawSky,
    onPrimaryContainer = PawDarkBlue,
    secondary = PawFireOrange,
    onSecondary = Color.White,
    secondaryContainer = PawSunYellow,
    onSecondaryContainer = Color(0xFF3E2723),
    tertiary = PawPink,
    onTertiary = Color.White,
    background = Color(0xFFF0F8FF), // Alice Blue
    onBackground = PawDarkText,
    surface = Color.White,
    onSurface = PawDarkText,
    surfaceVariant = Color(0xFFFFF9E6),
    onSurfaceVariant = PawDarkText
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PawColorScheme,
        typography = Typography,
        shapes = PawShapes,
        content = content
    )
}
