package com.Vantavu6.Sevlod.domain.model

enum class RowKind {
    Gate,
    Token,
    Cone,
    Border
}

data class RouteRow(
    val id: Int,
    val kind: RowKind,
    val positionDp: Float,
    val lanes: List<Int>
)

data class Route(
    val seed: Int,
    val rows: List<RouteRow>,
    val fallbackUsed: Boolean
)
