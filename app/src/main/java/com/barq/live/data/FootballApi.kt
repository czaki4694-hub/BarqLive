package com.barq.live.data

import com.barq.live.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

object FootballApi {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    /** مباريات اليوم من football-data.org (v4). */
    suspend fun todayMatches(): List<MatchDto> = withContext(Dispatchers.IO) {
        if (BuildConfig.FOOTBALL_API_KEY.isBlank()) {
            error("ضع FOOTBALL_API_KEY فـ local.properties")
        }
        val req = Request.Builder()
            .url("https://api.football-data.org/v4/matches")
            .header("X-Auth-Token", BuildConfig.FOOTBALL_API_KEY)
            .build()
        client.newCall(req).execute().use { r ->
            if (!r.isSuccessful) error("خطأ فالسيرفر (${r.code})")
            json.decodeFromString<MatchesResponse>(r.body!!.string()).matches
        }
    }
}

/**
 * مصدر البث. بدّل الرابط برابط HLS (.m3u8) ديالك اللي عندك الحق فيه.
 * الرابط الحالي مجرد فيديو تجريبي عمومي لاختبار المشغل.
 */
object StreamSource {
    private const val TEST_HLS = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

    @Suppress("UNUSED_PARAMETER")
    fun urlFor(match: MatchDto): String = TEST_HLS
}
