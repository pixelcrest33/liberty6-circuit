package com.Vantavu6.Sevlod.domain.usecase

import com.Vantavu6.Sevlod.core.config.GameConfig
import kotlin.math.min

class ScoreChainUseCase {

    fun multiplier(ticks: Int): Int =
        min(GameConfig.MAX_MULTIPLIER, 1 + ticks / GameConfig.TICKS_PER_MULTIPLIER)

    fun gateScore(ticks: Int, nitroActive: Boolean): Int {
        val base = GameConfig.GATE_BASE_SCORE * multiplier(ticks)
        return if (nitroActive) base * GameConfig.NITRO_SCORE_FACTOR else base
    }

    fun pickupScore(nitroActive: Boolean): Int =
        if (nitroActive) GameConfig.TOKEN_SCORE * GameConfig.NITRO_SCORE_FACTOR else GameConfig.TOKEN_SCORE

    fun bankScore(ticks: Int): Int = ticks * GameConfig.BANK_PER_TICK

    fun nitroArmed(ticks: Int): Boolean = ticks >= GameConfig.NITRO_ARM_TICKS
}
