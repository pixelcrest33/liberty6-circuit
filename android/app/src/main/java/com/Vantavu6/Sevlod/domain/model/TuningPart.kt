package com.Vantavu6.Sevlod.domain.model

data class TuningPart(
    val id: String,
    val maxLevel: Int,
    val costs: List<Int>
) {
    fun costForLevel(level: Int): Int = costs.getOrElse(level) { Int.MAX_VALUE }
}
