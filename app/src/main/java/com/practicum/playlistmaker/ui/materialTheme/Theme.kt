package com.practicum.playlistmaker.ui.materialTheme

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
// import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFF313131),
//    background = Color(0xFF000000),
//    surface = Color(0xFF1A1A1A),
//    onPrimary = Color.White,
//    onBackground = Color.White,
//    onSurface = Color.White,

)

private val LightColorScheme = lightColorScheme(
//    primary = Color(0xFF3772E7),
//    secondary = Color(0xFF9B9B9B),
//    tertiary = Color(0xFFFF4081),
//    background = Color(0xFF3772E7),
//    surface = Color.White,

)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
