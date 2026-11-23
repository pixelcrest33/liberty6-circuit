package com.Vantavu6.Sevlod.presentation.game

import com.Vantavu6.Sevlod.domain.model.RowKind
import com.Vantavu6.Sevlod.domain.model.RunResult

enum class GamePhase {
    PreRun,
    Running,
    Border,
    Crashing,
    Paused,
    Finished
}

enum class ActionLabel {
    Drift,
    Nitro,
    Bank
}

data class VisibleRow(
    val id: Int,
    val kind: RowKind,
    val offsetDp: Float,
    val lanes: List<Int>,
    val cut: Boolean,
    val alpha: Float
)

data class GameUiState(
    val phase: GamePhase = GamePhase.PreRun,
    val lane: Int = 1,
    val tilt: Float = 0f,
    val score: Int = 0,
    val chainTicks: Int = 0,
    val multiplier: Int = 1,
    val runTokens: Int = 0,
    val distanceDp: Float = 0f,
    val lengthDp: Float = 0f,
    val clockMs: Float = 0f,
    val clockTotalMs: Float = 1f,
    val nitroMsLeft: Float = 0f,
    val borderMsLeft: Float = 0f,
    val missFlashMs: Float = 0f,
    val crashFlash: Float = 0f,
    val hintVisible: Boolean = true,
    val controlSideRight: Boolean = false,
    val ghostVisible: Boolean = false,
    val ghostLane: Int = 1,
    val rows: List<VisibleRow> = emptyList(),
    val action: ActionLabel = ActionLabel.Drift
) {
    val nitroActive: Boolean get() = nitroMsLeft > 0f
    val clockFraction: Float get() = (clockMs / clockTotalMs).coerceIn(0f, 1f)
}

sealed interface GameEvent {
    data class Finished(val result: RunResult) : GameEvent
}
