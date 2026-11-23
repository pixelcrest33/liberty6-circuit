package com.Vantavu6.Sevlod.domain.repository

import com.Vantavu6.Sevlod.domain.model.GhostSample
import com.Vantavu6.Sevlod.domain.model.PlayerProgress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {

    val progress: StateFlow<PlayerProgress>

    fun selectDistrict(districtId: String)

    fun selectWeather(weatherId: String)

    fun selectMode(modeId: String)

    fun setGhostEnabled(enabled: Boolean)

    fun setControlSideRight(right: Boolean)

    fun spendTokens(amount: Int): Boolean

    fun setPartLevel(partId: String, level: Int)

    fun nextAttempt(): Int

    fun commitRun(
        districtId: String,
        score: Int,
        chain: Int,
        tokens: Int,
        ghost: List<GhostSample>
    ): Boolean
}
