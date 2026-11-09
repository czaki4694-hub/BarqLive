package com.barq.live.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.barq.live.data.MatchDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val electricBrush = Brush.linearGradient(listOf(Electric.Cyan, Electric.Violet))

@Composable
fun HomeScreen(onWatch: (MatchDto) -> Unit, vm: HomeViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Electric.Bg, Electric.Bg2)))
            .statusBarsPadding()
    ) {
        Header()
        when (val s = state) {
            UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = Electric.Cyan)
            }
            is UiState.Error -> Column(
                Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(s.message, color = Electric.Muted, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                GlowButton("إعادة المحاولة") { vm.load() }
            }
            is UiState.Success ->
                if (s.matches.isEmpty()) Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("لا توجد مباريات اليوم", color = Electric.Muted)
                } else LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    items(s.matches, key = { it.id }) { MatchCard(it) { onWatch(it) } }
                }
        }
    }
}

@Composable
private fun Header() {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            "برق لايف ⚡",
            style = TextStyle(
                brush = electricBrush,
                fontFamily = Cairo,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
            ),
        )
        Text("مباريات اليوم", color = Electric.Muted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun MatchCard(m: MatchDto, onClick: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(14.dp, shape, ambientColor = Electric.Cyan, spotColor = Electric.Violet)
            .background(Electric.Surface, shape)
            .border(BorderStroke(1.dp, electricBrush), shape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                m.competition.name,
                color = Electric.Muted,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f),
            )
            if (m.isLive) LivePill()
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TeamColumn(m.homeTeam.tla, m.homeTeam.shortName ?: m.homeTeam.name, Modifier.weight(1f))
            Center(m)
            TeamColumn(m.awayTeam.tla, m.awayTeam.shortName ?: m.awayTeam.name, Modifier.weight(1f))
        }
        Spacer(Modifier.height(14.dp))
        GlowButton(if (m.isLive) "شاهد الآن" else "التفاصيل", Modifier.fillMaxWidth(), onClick)
    }
}

@Composable
private fun TeamColumn(tla: String?, name: String?, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(54.dp).border(BorderStroke(2.dp, electricBrush), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(tla ?: "?", fontFamily = Cairo, fontSize = 15.sp, color = Electric.Cyan,
                fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(6.dp))
        Text(name ?: "-", maxLines = 1, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}

@Composable
private fun Center(m: MatchDto) {
    Column(Modifier.width(92.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (m.isLive || m.isFinished) {
            val h = m.score.fullTime.home ?: 0
            val a = m.score.fullTime.away ?: 0
            Text("$h - $a", style = TextStyle(brush = electricBrush, fontFamily = Cairo, fontSize = 28.sp,
                fontWeight = FontWeight.Black))
            if (m.isFinished) Text("انتهت", color = Electric.Muted, fontSize = 12.sp)
        } else {
            Text(kickoff(m.utcDate), style = TextStyle(color = Electric.Cyan, fontFamily = Cairo, fontSize = 22.sp,
                fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
private fun LivePill() {
    val t = rememberInfiniteTransition(label = "pulse")
    val a by t.animateFloat(0.25f, 1f, infiniteRepeatable(tween(750), RepeatMode.Reverse), label = "a")
    Row(
        Modifier.background(Electric.Lime.copy(alpha = 0.15f), RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).alpha(a).background(Electric.Lime, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text("مباشر", color = Electric.Lime, fontSize = 12.sp, fontFamily = Cairo,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GlowButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier
            .background(electricBrush, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Electric.Bg, fontFamily = Cairo,
            fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

private fun kickoff(utc: String): String = runCatching {
    DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
        .withZone(ZoneId.systemDefault())
        .format(Instant.parse(utc))
}.getOrDefault("--:--")
