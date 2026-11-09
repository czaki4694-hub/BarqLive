package com.barq.live

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.barq.live.data.MatchDto
import com.barq.live.ui.BarqTheme
import com.barq.live.ui.HomeScreen
import com.barq.live.ui.PlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                BarqTheme {
                    var selected by remember { mutableStateOf<MatchDto?>(null) }
                    val m = selected
                    if (m == null) HomeScreen(onWatch = { selected = it })
                    else PlayerScreen(m, onBack = { selected = null })
                }
            }
        }
    }
}
