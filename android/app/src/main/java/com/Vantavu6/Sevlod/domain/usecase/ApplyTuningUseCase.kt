package com.Vantavu6.Sevlod.domain.usecase

import com.Vantavu6.Sevlod.core.config.GameConfig
import com.Vantavu6.Sevlod.domain.model.RunSetup

data class TuningEffect(
    val laneChangeMs: Float,
    val obstacleFadeDp: Float,
    val nitroDurationMs: Float
)

class ApplyTuningUseCase {

    operator fun invoke(setup: RunSetup): TuningEffect {
        val gripFactor = GRIP_FACTORS.getOrElse(setup.gripLevel) { GRIP_FACTORS.last() }
        val headlightFactor = HEADLIGHT_FACTORS.getOrElse(setup.headlightLevel) { HEADLIGHT_FACTORS.last() }
        val laneChangeMs = GameConfig.LANE_CHANGE_BASE_MS * setup.weather.laneChangeFactor * gripFactor
        val fadeDp = GameConfig.OBSTACLE_FADE_DP * setup.weather.fadeFactor * headlightFactor
        val nitroMs = GameConfig.NITRO_BASE_MS + GameConfig.NITRO_PER_LEVEL_MS * setup.nitroLevel
        return TuningEffect(laneChangeMs, fadeDp, nitroMs)
    }

    companion object {
        val GRIP_FACTORS = listOf(1f, 0.85f, 0.72f, 0.61f)
        val HEADLIGHT_FACTORS = listOf(1f, 1.12f, 1.24f, 1.36f)
    }
}
