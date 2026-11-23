package com.Vantavu6.Sevlod.core.config

object GameConfig {

    const val LOADER_DURATION_MS = 8000L

    const val TICK_MS = 16L
    const val LANE_COUNT = 3
    const val START_LANE = 1

    const val LANE_CHANGE_BASE_MS = 180f
    const val GHOST_SAMPLE_MS = 100L

    const val GATE_BAND_DP = 56f
    const val PICKUP_BAND_DP = 30f
    const val CONE_BAND_DP = 34f
    const val NEAR_MISS_BAND_DP = 40f

    const val GATE_BASE_SCORE = 50
    const val TOKEN_SCORE = 10
    const val BANK_PER_TICK = 15
    const val DAWN_CLEAR_BONUS = 400

    const val MAX_MULTIPLIER = 8
    const val TICKS_PER_MULTIPLIER = 3
    const val NITRO_ARM_TICKS = 15
    const val NITRO_BASE_MS = 6000f
    const val NITRO_PER_LEVEL_MS = 1500f
    const val NITRO_SPEED_FACTOR = 1.35f
    const val NITRO_SCORE_FACTOR = 2

    const val BORDER_WINDOW_MS = 2500f
    const val BORDER_COUNT = 2

    const val DAWN_RUN_MS = 75000f
    const val TIME_TRIAL_MS = 60000f

    const val CRASH_ANIM_MS = 900L
    const val SAFE_OPENING_MS = 60000f

    const val HINT_VISIBLE_MS = 6000f
    const val OBSTACLE_FADE_DP = 380f

    const val ROUTE_ATTEMPTS = 20
    const val SEED_DISTRICT_FACTOR = 7919
    const val SEED_ATTEMPT_FACTOR = 104729
}
