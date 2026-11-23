package com.Vantavu6.Sevlod.presentation.garage

data class TuningRowState(
    val id: String,
    val level: Int,
    val maxLevel: Int,
    val nextCost: Int,
    val affordable: Boolean
) {
    val maxed: Boolean get() = level >= maxLevel
}

data class GarageUiState(
    val tokens: Int = 0,
    val rows: List<TuningRowState> = emptyList(),
    val installedPartId: String = ""
)
