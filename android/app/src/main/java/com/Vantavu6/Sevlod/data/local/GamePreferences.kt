package com.Vantavu6.Sevlod.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Vantavu6.Sevlod.domain.model.GhostSample
import com.Vantavu6.Sevlod.domain.model.PlayerProgress

class GamePreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun read(): PlayerProgress = PlayerProgress(
        totalTokens = prefs.getInt(KEY_TOKENS, 0),
        bestScores = decodeInts(prefs.getString(KEY_BEST_SCORES, "").orEmpty()),
        bestChain = prefs.getInt(KEY_BEST_CHAIN, 0),
        partLevels = decodeInts(prefs.getString(KEY_PART_LEVELS, "").orEmpty()),
        ghosts = decodeGhosts(prefs.getString(KEY_GHOSTS, "").orEmpty()),
        lastDistrictId = prefs.getString(KEY_LAST_DISTRICT, "").orEmpty(),
        lastWeatherId = prefs.getString(KEY_LAST_WEATHER, "").orEmpty(),
        lastModeId = prefs.getString(KEY_LAST_MODE, "").orEmpty(),
        ghostEnabled = prefs.getBoolean(KEY_GHOST_ENABLED, true),
        controlSideRight = prefs.getBoolean(KEY_CONTROL_SIDE, false),
        attempts = prefs.getInt(KEY_ATTEMPTS, 0)
    )

    fun write(progress: PlayerProgress) {
        prefs.edit()
            .putInt(KEY_TOKENS, progress.totalTokens)
            .putString(KEY_BEST_SCORES, encodeInts(progress.bestScores))
            .putInt(KEY_BEST_CHAIN, progress.bestChain)
            .putString(KEY_PART_LEVELS, encodeInts(progress.partLevels))
            .putString(KEY_GHOSTS, encodeGhosts(progress.ghosts))
            .putString(KEY_LAST_DISTRICT, progress.lastDistrictId)
            .putString(KEY_LAST_WEATHER, progress.lastWeatherId)
            .putString(KEY_LAST_MODE, progress.lastModeId)
            .putBoolean(KEY_GHOST_ENABLED, progress.ghostEnabled)
            .putBoolean(KEY_CONTROL_SIDE, progress.controlSideRight)
            .putInt(KEY_ATTEMPTS, progress.attempts)
            .apply()
    }

    private fun encodeInts(values: Map<String, Int>): String =
        values.entries.joinToString(ENTRY_SEP) { "${it.key}$PAIR_SEP${it.value}" }

    private fun decodeInts(raw: String): Map<String, Int> {
        if (raw.isBlank()) return emptyMap()
        return raw.split(ENTRY_SEP).mapNotNull { entry ->
            val parts = entry.split(PAIR_SEP)
            val value = parts.getOrNull(1)?.toIntOrNull()
            if (parts.size == 2 && value != null) parts[0] to value else null
        }.toMap()
    }

    private fun encodeGhosts(ghosts: Map<String, List<GhostSample>>): String =
        ghosts.entries.joinToString(ENTRY_SEP) { entry ->
            val body = entry.value.joinToString(SAMPLE_SEP) { "${it.distanceDp.toInt()}$FIELD_SEP${it.lane}" }
            "${entry.key}$PAIR_SEP$body"
        }

    private fun decodeGhosts(raw: String): Map<String, List<GhostSample>> {
        if (raw.isBlank()) return emptyMap()
        return raw.split(ENTRY_SEP).mapNotNull { entry ->
            val parts = entry.split(PAIR_SEP)
            if (parts.size != 2) return@mapNotNull null
            val samples = parts[1].split(SAMPLE_SEP).mapNotNull { sample ->
                val fields = sample.split(FIELD_SEP)
                val distance = fields.getOrNull(0)?.toFloatOrNull()
                val lane = fields.getOrNull(1)?.toIntOrNull()
                if (distance != null && lane != null) GhostSample(distance, lane) else null
            }
            if (samples.isEmpty()) null else parts[0] to samples
        }.toMap()
    }

    private companion object {
        const val STORE = "liberty6_circuit_progress"
        const val KEY_TOKENS = "total_tokens"
        const val KEY_BEST_SCORES = "best_scores"
        const val KEY_BEST_CHAIN = "best_chain"
        const val KEY_PART_LEVELS = "part_levels"
        const val KEY_GHOSTS = "ghosts"
        const val KEY_LAST_DISTRICT = "last_district"
        const val KEY_LAST_WEATHER = "last_weather"
        const val KEY_LAST_MODE = "last_mode"
        const val KEY_GHOST_ENABLED = "ghost_enabled"
        const val KEY_CONTROL_SIDE = "control_side_right"
        const val KEY_ATTEMPTS = "attempts"
        const val ENTRY_SEP = "|"
        const val PAIR_SEP = ":"
        const val SAMPLE_SEP = ";"
        const val FIELD_SEP = ","
    }
}
