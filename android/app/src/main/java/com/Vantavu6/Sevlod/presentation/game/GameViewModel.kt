package com.Vantavu6.Sevlod.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vantavu6.Sevlod.core.config.GameConfig
import com.Vantavu6.Sevlod.data.sample.SampleData
import com.Vantavu6.Sevlod.domain.model.GhostSample
import com.Vantavu6.Sevlod.domain.model.RouteRow
import com.Vantavu6.Sevlod.domain.model.RowKind
import com.Vantavu6.Sevlod.domain.model.RunMode
import com.Vantavu6.Sevlod.domain.model.RunSetup
import com.Vantavu6.Sevlod.domain.model.RunVerdict
import com.Vantavu6.Sevlod.domain.repository.CatalogRepository
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import com.Vantavu6.Sevlod.domain.usecase.ApplyTuningUseCase
import com.Vantavu6.Sevlod.domain.usecase.GenerateRouteUseCase
import com.Vantavu6.Sevlod.domain.usecase.SaveRunResultUseCase
import com.Vantavu6.Sevlod.domain.usecase.ScoreChainUseCase
import com.Vantavu6.Sevlod.domain.usecase.TuningEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository,
    private val generateRoute: GenerateRouteUseCase,
    private val applyTuning: ApplyTuningUseCase,
    private val scoreChain: ScoreChainUseCase,
    private val saveRunResult: SaveRunResultUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var loop: Job? = null
    private var setup: RunSetup? = null
    private var effect: TuningEffect = TuningEffect(
        GameConfig.LANE_CHANGE_BASE_MS,
        GameConfig.OBSTACLE_FADE_DP,
        GameConfig.NITRO_BASE_MS
    )
    private var rows: List<RouteRow> = emptyList()
    private val consumed = mutableSetOf<Int>()
    private val cutGates = mutableSetOf<Int>()
    private val ghostTrack = mutableListOf<GhostSample>()
    private var ghostReplay: List<GhostSample> = emptyList()
    private var sampleTimerMs = 0f
    private var laneChangeLeftMs = 0f
    private var previousLane = GameConfig.START_LANE
    private var crashTimerMs = 0f
    private var hintLeftMs = GameConfig.HINT_VISIBLE_MS
    private var bestChain = 0
    private var speed = 0f

    init {
        prepareRun()
    }

    fun prepareRun() {
        loop?.cancel()
        loop = null
        val progress = progressRepository.progress.value
        val district = catalogRepository.districtOrDefault(progress.lastDistrictId)
        val weather = catalogRepository.weatherOrDefault(progress.lastWeatherId)
        val mode = RunMode.fromId(progress.lastModeId)
        val runSetup = RunSetup(
            district = district,
            weather = weather,
            mode = mode,
            gripLevel = progress.levelOf(SampleData.PART_GRIP),
            headlightLevel = progress.levelOf(SampleData.PART_HEADLIGHT),
            nitroLevel = progress.levelOf(SampleData.PART_NITRO),
            ghostEnabled = progress.ghostEnabled,
            controlSideRight = progress.controlSideRight,
            attempt = progress.attempts
        )
        setup = runSetup
        effect = applyTuning(runSetup)
        ghostReplay = if (runSetup.ghostEnabled) progress.ghostFor(district.id) else emptyList()
        rows = emptyList()
        consumed.clear()
        cutGates.clear()
        ghostTrack.clear()
        sampleTimerMs = 0f
        laneChangeLeftMs = 0f
        previousLane = GameConfig.START_LANE
        crashTimerMs = 0f
        hintLeftMs = if (progress.attempts == 0) Float.MAX_VALUE else GameConfig.HINT_VISIBLE_MS
        bestChain = 0
        speed = district.startSpeedDp
        val clock = if (mode == RunMode.TimeTrial) GameConfig.TIME_TRIAL_MS else GameConfig.DAWN_RUN_MS
        _state.value = GameUiState(
            phase = GamePhase.PreRun,
            lane = GameConfig.START_LANE,
            lengthDp = district.lengthDp,
            clockMs = clock,
            clockTotalMs = clock,
            hintVisible = true,
            controlSideRight = runSetup.controlSideRight,
            ghostVisible = ghostReplay.isNotEmpty(),
            ghostLane = GameConfig.START_LANE,
            action = ActionLabel.Drift
        )
    }

    fun start() {
        val runSetup = setup ?: return
        if (_state.value.phase != GamePhase.PreRun) return
        val attempt = progressRepository.nextAttempt()
        val seeded = runSetup.copy(attempt = attempt)
        setup = seeded
        rows = generateRoute(seeded, effect.laneChangeMs).rows
        _state.update { it.copy(phase = GamePhase.Running) }
        startLoop()
    }

    fun pause() {
        loop?.cancel()
        loop = null
        val phase = _state.value.phase
        if (phase == GamePhase.Running || phase == GamePhase.Border) {
            _state.update { it.copy(phase = GamePhase.Paused) }
        }
    }

    fun resume() {
        if (_state.value.phase != GamePhase.Paused) return
        _state.update { it.copy(phase = GamePhase.Running) }
        startLoop()
    }

    fun restart() {
        prepareRun()
        start()
    }

    fun moveLeft() = changeLane(-1)

    fun moveRight() = changeLane(1)

    fun primaryAction() {
        val current = _state.value
        when (current.phase) {
            GamePhase.Border -> bank()
            GamePhase.Running -> if (scoreChain.nitroArmed(current.chainTicks)) armNitro() else drift()
            else -> Unit
        }
    }

    private fun changeLane(delta: Int) {
        val current = _state.value
        if (current.phase != GamePhase.Running && current.phase != GamePhase.Border) return
        if (laneChangeLeftMs > 0f) return
        val target = (current.lane + delta).coerceIn(0, GameConfig.LANE_COUNT - 1)
        if (target == current.lane) return
        previousLane = current.lane
        laneChangeLeftMs = effect.laneChangeMs
        _state.update { it.copy(lane = target, tilt = delta * 9f) }
    }

    private fun drift() {
        val current = _state.value
        val gate = rows.firstOrNull { row ->
            row.kind == RowKind.Gate &&
                !consumed.contains(row.id) &&
                !cutGates.contains(row.id) &&
                abs(row.positionDp - current.distanceDp) <= GameConfig.GATE_BAND_DP &&
                row.lanes.contains(current.lane)
        }
        if (gate == null) {
            _state.update { it.copy(missFlashMs = 260f) }
            return
        }
        cutGates.add(gate.id)
        val gained = scoreChain.gateScore(current.chainTicks, current.nitroActive)
        val ticks = current.chainTicks + 1
        bestChain = max(bestChain, ticks)
        _state.update {
            it.copy(
                score = it.score + gained,
                chainTicks = ticks,
                multiplier = scoreChain.multiplier(ticks),
                rows = it.rows.map { row -> if (row.id == gate.id) row.copy(cut = true) else row },
                action = actionFor(it.phase, ticks)
            )
        }
    }

    private fun armNitro() {
        val current = _state.value
        _state.update {
            it.copy(
                nitroMsLeft = effect.nitroDurationMs,
                chainTicks = 0,
                multiplier = 1,
                action = ActionLabel.Drift
            )
        }
        bestChain = max(bestChain, current.chainTicks)
    }

    private fun bank() {
        val current = _state.value
        val gained = scoreChain.bankScore(current.chainTicks)
        bestChain = max(bestChain, current.chainTicks)
        _state.update {
            it.copy(
                score = it.score + gained,
                chainTicks = 0,
                multiplier = 1,
                borderMsLeft = 0f,
                phase = GamePhase.Running,
                action = ActionLabel.Drift
            )
        }
    }

    private fun startLoop() {
        loop?.cancel()
        loop = viewModelScope.launch {
            var last = System.nanoTime()
            while (isActive) {
                delay(GameConfig.TICK_MS)
                val now = System.nanoTime()
                val dt = ((now - last) / 1_000_000f).coerceIn(1f, 48f)
                last = now
                tick(dt)
            }
        }
    }

    private fun tick(dtMs: Float) {
        val runSetup = setup ?: return
        val current = _state.value
        if (current.phase == GamePhase.Crashing) {
            crashTimerMs -= dtMs
            val fraction = (crashTimerMs / GameConfig.CRASH_ANIM_MS).coerceIn(0f, 1f)
            _state.update { it.copy(crashFlash = fraction, tilt = 24f * (1f - fraction)) }
            if (crashTimerMs <= 0f) {
                finish(RunVerdict.Crashed)
            }
            return
        }
        if (current.phase != GamePhase.Running && current.phase != GamePhase.Border) return

        val district = runSetup.district
        val ramp = if (runSetup.mode == RunMode.TimeTrial) {
            1f - current.clockFraction
        } else {
            (current.distanceDp / district.lengthDp).coerceIn(0f, 1f)
        }
        val baseSpeed = district.startSpeedDp + (district.endSpeedDp - district.startSpeedDp) * ramp
        speed = if (current.nitroActive) baseSpeed * GameConfig.NITRO_SPEED_FACTOR else baseSpeed

        val distance = current.distanceDp + speed * dtMs / 1000f
        val clock = current.clockMs - dtMs
        if (laneChangeLeftMs > 0f) laneChangeLeftMs -= dtMs
        if (hintLeftMs != Float.MAX_VALUE) hintLeftMs -= dtMs
        sampleTimerMs += dtMs
        if (sampleTimerMs >= GameConfig.GHOST_SAMPLE_MS) {
            sampleTimerMs = 0f
            ghostTrack.add(GhostSample(distance, current.lane))
        }

        var score = current.score
        var ticks = current.chainTicks
        var tokens = current.runTokens
        var phase = current.phase
        var borderLeft = max(0f, current.borderMsLeft - dtMs)
        if (phase == GamePhase.Border && borderLeft <= 0f) phase = GamePhase.Running
        var crashed = false

        val occupied = if (laneChangeLeftMs > 0f) {
            setOf(current.lane, previousLane)
        } else {
            setOf(current.lane)
        }

        rows.forEach { row ->
            if (consumed.contains(row.id)) return@forEach
            if (row.positionDp - distance > -CONSUME_MARGIN_DP) return@forEach
            consumed.add(row.id)
            when (row.kind) {
                RowKind.Gate -> if (!cutGates.contains(row.id)) {
                    ticks = 0
                }

                RowKind.Token -> if (row.lanes.any { occupied.contains(it) }) {
                    tokens += 1
                    score += scoreChain.pickupScore(current.nitroActive)
                }

                RowKind.Cone -> if (row.lanes.any { occupied.contains(it) }) {
                    crashed = true
                } else if (row.lanes.any { abs(it - current.lane) == 1 }) {
                    ticks += 1
                }

                RowKind.Border -> {
                    phase = GamePhase.Border
                    borderLeft = GameConfig.BORDER_WINDOW_MS
                }
            }
        }

        bestChain = max(bestChain, ticks)
        val multiplier = scoreChain.multiplier(ticks)
        val nitroLeft = max(0f, current.nitroMsLeft - dtMs)
        val missLeft = max(0f, current.missFlashMs - dtMs)
        val ghostLane = ghostLaneAt(distance)
        val visible = visibleRows(distance)

        _state.update {
            it.copy(
                phase = phase,
                distanceDp = distance,
                clockMs = max(0f, clock),
                score = score,
                chainTicks = ticks,
                multiplier = multiplier,
                runTokens = tokens,
                nitroMsLeft = nitroLeft,
                borderMsLeft = borderLeft,
                missFlashMs = missLeft,
                tilt = it.tilt * 0.86f,
                hintVisible = hintLeftMs > 0f,
                ghostLane = ghostLane,
                rows = visible,
                action = actionFor(phase, ticks)
            )
        }

        if (crashed) {
            crashTimerMs = GameConfig.CRASH_ANIM_MS.toFloat()
            _state.update { it.copy(phase = GamePhase.Crashing, crashFlash = 1f) }
            return
        }
        if (clock <= 0f) {
            val verdict = if (runSetup.mode == RunMode.TimeTrial) RunVerdict.ClockOut else RunVerdict.SunCaught
            finish(verdict)
            return
        }
        if (runSetup.mode == RunMode.DawnRun && distance >= district.lengthDp) {
            _state.update { it.copy(score = it.score + GameConfig.DAWN_CLEAR_BONUS) }
            finish(RunVerdict.DawnCleared)
        }
    }

    private fun actionFor(phase: GamePhase, ticks: Int): ActionLabel = when {
        phase == GamePhase.Border -> ActionLabel.Bank
        scoreChain.nitroArmed(ticks) -> ActionLabel.Nitro
        else -> ActionLabel.Drift
    }

    private fun ghostLaneAt(distance: Float): Int {
        if (ghostReplay.isEmpty()) return GameConfig.START_LANE
        val sample = ghostReplay.lastOrNull { it.distanceDp <= distance } ?: ghostReplay.first()
        return sample.lane
    }

    private fun visibleRows(distance: Float): List<VisibleRow> {
        val fade = effect.obstacleFadeDp
        return rows.mapNotNull { row ->
            val offset = row.positionDp - distance
            if (offset < -CONSUME_MARGIN_DP || offset > VIEW_AHEAD_DP) return@mapNotNull null
            if (row.kind == RowKind.Border) return@mapNotNull null
            val alpha = ((fade + FADE_RAMP_DP - offset) / FADE_RAMP_DP).coerceIn(0f, 1f)
            VisibleRow(
                id = row.id,
                kind = row.kind,
                offsetDp = offset,
                lanes = row.lanes,
                cut = cutGates.contains(row.id),
                alpha = alpha
            )
        }
    }

    private fun finish(verdict: RunVerdict) {
        loop?.cancel()
        loop = null
        val runSetup = setup ?: return
        val current = _state.value
        val result = saveRunResult(
            districtId = runSetup.district.id,
            modeId = runSetup.mode.id,
            verdict = verdict,
            score = current.score,
            chain = bestChain,
            tokens = current.runTokens,
            distanceDp = current.distanceDp.toInt(),
            ghost = ghostTrack.toList()
        )
        _state.update { it.copy(phase = GamePhase.Finished) }
        viewModelScope.launch { _events.send(GameEvent.Finished(result)) }
    }

    override fun onCleared() {
        loop?.cancel()
        loop = null
        super.onCleared()
    }

    private companion object {
        const val CONSUME_MARGIN_DP = 24f
        const val VIEW_AHEAD_DP = 900f
        const val FADE_RAMP_DP = 140f
    }
}
