package com.barq.live.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.barq.live.data.MatchDto
import com.barq.live.data.StreamSource

@Composable
fun PlayerScreen(match: MatchDto, onBack: () -> Unit) {
    val ctx = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(ctx).build().apply {
            setMediaItem(MediaItem.fromUri(StreamSource.urlFor(match)))
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(Unit) { onDispose { player.release() } }
    BackHandler(onBack = onBack)

    Column(Modifier.fillMaxSize().background(Electric.Bg).statusBarsPadding()) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            AndroidView(
                factory = { PlayerView(it).apply { this.player = player } },
                modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
            )
        }
        Column(Modifier.padding(20.dp)) {
            Text(match.competition.name, color = Electric.Muted, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            val home = match.homeTeam.shortName ?: match.homeTeam.name ?: ""
            val away = match.awayTeam.shortName ?: match.awayTeam.name ?: ""
            Text("$home ضد $away", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            GlowButton("رجوع", onClick = onBack)
        }
    }
}
