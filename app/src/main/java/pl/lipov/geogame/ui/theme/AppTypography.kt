package pl.lipov.geogame.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import pl.lipov.geogame.R

private val retroFont = FontFamily(
    Font(R.font.press_start_2p, weight = FontWeight.Normal)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = retroFont,
        fontSize = Dimens.FontSizeLarge
    ),
    bodyMedium = TextStyle(
        fontFamily = retroFont,
        fontSize = Dimens.FontSizeMedium
    ),
    bodySmall = TextStyle(
        fontFamily = retroFont,
        fontSize = Dimens.FontSizeSmall
    )
)
