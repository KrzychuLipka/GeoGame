package pl.lipov.geogame.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RetroColorScheme = darkColorScheme(
    primary = Color.Red,// główny kolor aplikacji (przyciski itp.)
    onPrimary = Color.Yellow,// kolor tekstu lub ikon, które pojawiają się na tle primary
    secondary = Color.Red,// elementy "drugorzędne" (panele boczne; mniej ważne przyciski; itp.)
    onSecondary = Color.Yellow,// kolor tekstu/ikon na tle secondary
    tertiary = Color.Yellow,// elementy "trzeciorzędne" (karty; ramki; separatory; itp.)
    onTertiary = Color.Yellow,// kolor tekstu/ikon na tle tertiary
    background = Color.Black,// kolor tła całej aplikacji
    onBackground = Color.Yellow,// kolor tekstu/ikon na tle background
    surface = Color.Black,// kolor powierzchni (karty, dialogi, bottom sheety, itp.)
    onSurface = Color.Yellow,// kolor tekstu/ikon na tle surface
    outline = Color.Yellow,// kolor obramowań, ramek, dividerów
    error = Color.Red// kolor błędów
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RetroColorScheme,
        typography = AppTypography,
        content = content
    )
}
