package com.Vantavu6.Sevlod.data.repository

import com.Vantavu6.Sevlod.data.local.GamePreferences
import com.Vantavu6.Sevlod.domain.model.GhostSample
import com.Vantavu6.Sevlod.domain.model.PlayerProgress
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max

class ProgressRepositoryImpl(private val preferences: GamePreferences) : ProgressRepository {

    private val state = MutableStateFlow(preferences.read())

    override val progress: StateFlow<PlayerProgress> = state.asStateFlow()

    override fun selectDistrict(districtId: String) = mutate { it.copy(lastDistrictId = districtId) }

    override fun selectWeather(weatherId: String) = mutate { it.copy(lastWeatherId = weatherId) }

    override fun selectMode(modeId: String) = mutate { it.copy(lastModeId = modeId) }

    override fun setGhostEnabled(enabled: Boolean) = mutate { it.copy(ghostEnabled = enabled) }

    override fun setControlSideRight(right: Boolean) = mutate { it.copy(controlSideRight = right) }

    override fun spendTokens(amount: Int): Boolean {
        if (state.value.totalTokens < amount) return false
        mutate { it.copy(totalTokens = it.totalTokens - amount) }
        return true
    }

    override fun setPartLevel(partId: String, level: Int) = mutate {
        it.copy(partLevels = it.partLevels + (partId to level))
    }

    override fun nextAttempt(): Int {
        val next = state.value.attempts + 1
        mutate { it.copy(attempts = next) }
        return next
    }

    override fun commitRun(
        districtId: String,
        score: Int,
        chain: Int,
        tokens: Int,
        ghost: List<GhostSample>
    ): Boolean {
        val current = state.value
        val previousBest = current.bestFor(districtId)
        val record = score > previousBest
        mutate { progress ->
            progress.copy(
                totalTokens = progress.totalTokens + tokens,
                bestScores = progress.bestScores + (districtId to max(previousBest, score)),
                bestChain = max(progress.bestChain, chain),
                ghosts = if (record && ghost.isNotEmpty()) {
                    progress.ghosts + (districtId to ghost)
                } else {
                    progress.ghosts
                }
            )
        }
        return record
    }

    private fun mutate(block: (PlayerProgress) -> PlayerProgress) {
        val updated = block(state.value)
        state.value = updated
        preferences.write(updated)
    }
}
