package com.barq.live.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.barq.live.R

object Electric {
    val Bg = Color(0xFF050816)
    val Bg2 = Color(0xFF0A0F2E)
    val Surface = Color(0xFF0D1330)
    val Cyan = Color(0xFF00E5FF)
    val Violet = Color(0xFF7C4DFF)
    val Lime = Color(0xFFC6FF00)
    val Text = Color(0xFFEAF2FF)
    val Muted = Color(0xFF8A96BF)
}

val Cairo = FontFamily(
    Font(R.font.cairo_regular, FontWeight.Normal),
    Font(R.font.cairo_bold, FontWeight.Bold),
    Font(R.font.cairo_black, FontWeight.Black),
)

private fun t(size: Int, w: FontWeight) =
    TextStyle(fontFamily = Cairo, fontWeight = w, fontSize = size.sp, color = Electric.Text)

private val AppTypography = Typography(
    headlineLarge = t(30, FontWeight.Black),
    titleLarge = t(22, FontWeight.Bold),
    titleMedium = t(17, FontWeight.Bold),
    bodyLarge = t(16, FontWeight.Normal),
    bodyMedium = t(14, FontWeight.Normal),
    labelLarge = t(13, FontWeight.Bold),
)

@Composable
fun BarqTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Electric.Cyan,
            secondary = Electric.Violet,
            background = Electric.Bg,
            surface = Electric.Surface,
            onBackground = Electric.Text,
            onSurface = Electric.Text,
        ),
        typography = AppTypography,
        content = content,
    )
}
