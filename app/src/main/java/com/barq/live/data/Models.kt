package com.barq.live.data

import kotlinx.serialization.Serializable

@Serializable
data class MatchesResponse(val matches: List<MatchDto> = emptyList())

@Serializable
data class MatchDto(
    val id: Int,
    val utcDate: String,
    val status: String,
    val competition: CompetitionDto,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val score: ScoreDto = ScoreDto(),
) {
    val isLive get() = status == "IN_PLAY" || status == "PAUSED" || status == "LIVE"
    val isFinished get() = status == "FINISHED"
}

@Serializable
data class CompetitionDto(val name: String = "")

@Serializable
data class TeamDto(
    val name: String? = null,
    val shortName: String? = null,
    val tla: String? = null,
)

@Serializable
data class ScoreDto(val fullTime: TimeScore = TimeScore())

@Serializable
data class TimeScore(val home: Int? = null, val away: Int? = null)
