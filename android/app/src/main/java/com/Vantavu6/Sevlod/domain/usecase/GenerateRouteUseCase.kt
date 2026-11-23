package com.Vantavu6.Sevlod.domain.usecase

import com.Vantavu6.Sevlod.core.config.GameConfig
import com.Vantavu6.Sevlod.domain.model.Route
import com.Vantavu6.Sevlod.domain.model.RouteRow
import com.Vantavu6.Sevlod.domain.model.RowKind
import com.Vantavu6.Sevlod.domain.model.RunSetup
import kotlin.math.max
import kotlin.random.Random

class GenerateRouteUseCase {

    operator fun invoke(setup: RunSetup, laneChangeMs: Float): Route {
        val seed = setup.district.index * GameConfig.SEED_DISTRICT_FACTOR xor
            (setup.attempt * GameConfig.SEED_ATTEMPT_FACTOR)
        var attemptSeed = seed
        repeat(GameConfig.ROUTE_ATTEMPTS) {
            val rows = build(setup, Random(attemptSeed))
            if (isSolvable(rows, setup, laneChangeMs)) {
                return Route(seed, rows, false)
            }
            attemptSeed += 31
        }
        return Route(seed, fallback(setup), true)
    }

    private fun build(setup: RunSetup, random: Random): List<RouteRow> {
        val rows = mutableListOf<RouteRow>()
        val district = setup.district
        val length = district.lengthDp
        val safeUntil = GameConfig.SAFE_OPENING_MS / 1000f * district.startSpeedDp
        var cursor = START_OFFSET_DP
        var id = 0
        while (cursor < length) {
            val jitter = 1f + (random.nextFloat() - 0.5f) * 0.36f
            val step = district.gateSpacingDp * jitter
            val gateLane = random.nextInt(GameConfig.LANE_COUNT)
            rows += RouteRow(id++, RowKind.Gate, cursor, listOf(gateLane))

            if (random.nextFloat() < 0.72f) {
                val tokenLane = random.nextInt(GameConfig.LANE_COUNT)
                rows += RouteRow(id++, RowKind.Token, cursor + step * 0.32f, listOf(tokenLane))
            }

            val coneChance = 0.55f * district.coneDensity
            if (random.nextFloat() < coneChance) {
                val coneCount = if (random.nextFloat() < 0.3f * district.coneDensity) 2 else 1
                val lanes = pickConeLanes(random, coneCount, cursor + step * 0.66f < safeUntil)
                if (lanes.isNotEmpty()) {
                    rows += RouteRow(id++, RowKind.Cone, cursor + step * 0.66f, lanes)
                }
            }
            cursor += step
        }
        val borderStep = length / (GameConfig.BORDER_COUNT + 1)
        for (i in 1..GameConfig.BORDER_COUNT) {
            rows += RouteRow(id++, RowKind.Border, borderStep * i, emptyList())
        }
        return rows.sortedBy { it.positionDp }
    }

    private fun pickConeLanes(random: Random, count: Int, keepCentreOpen: Boolean): List<Int> {
        val pool = (0 until GameConfig.LANE_COUNT).toMutableList()
        if (keepCentreOpen) pool.remove(GameConfig.START_LANE)
        val taken = mutableListOf<Int>()
        repeat(max(1, count)) {
            if (pool.size > 1) {
                taken += pool.removeAt(random.nextInt(pool.size))
            }
        }
        return taken
    }

    private fun isSolvable(rows: List<RouteRow>, setup: RunSetup, laneChangeMs: Float): Boolean {
        val cones = rows.filter { it.kind == RowKind.Cone }
        if (cones.any { it.lanes.size >= GameConfig.LANE_COUNT }) return false
        val safeUntil = GameConfig.SAFE_OPENING_MS / 1000f * setup.district.startSpeedDp
        if (cones.any { it.positionDp < safeUntil && it.lanes.contains(GameConfig.START_LANE) }) return false
        val reachDp = setup.district.endSpeedDp * (laneChangeMs / 1000f) * 1.6f
        var previous: RouteRow? = null
        for (cone in cones) {
            val last = previous
            if (last != null && cone.positionDp - last.positionDp < reachDp) {
                val blocked = (last.lanes + cone.lanes).toSet()
                if (blocked.size >= GameConfig.LANE_COUNT) return false
            }
            previous = cone
        }
        return true
    }

    private fun fallback(setup: RunSetup): List<RouteRow> {
        val rows = mutableListOf<RouteRow>()
        var cursor = START_OFFSET_DP
        var id = 0
        var left = true
        while (cursor < setup.district.lengthDp) {
            rows += RouteRow(id++, RowKind.Gate, cursor, listOf(if (left) 0 else 2))
            rows += RouteRow(id++, RowKind.Token, cursor + 60f, listOf(GameConfig.START_LANE))
            rows += RouteRow(id++, RowKind.Cone, cursor + 120f, listOf(if (left) 2 else 0))
            cursor += FALLBACK_SPACING_DP
            left = !left
        }
        val borderStep = setup.district.lengthDp / (GameConfig.BORDER_COUNT + 1)
        for (i in 1..GameConfig.BORDER_COUNT) {
            rows += RouteRow(id++, RowKind.Border, borderStep * i, emptyList())
        }
        return rows.sortedBy { it.positionDp }
    }

    companion object {
        const val START_OFFSET_DP = 420f
        const val FALLBACK_SPACING_DP = 160f
    }
}
