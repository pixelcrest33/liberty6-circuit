package com.Vantavu6.Sevlod.domain.usecase

import com.Vantavu6.Sevlod.domain.model.GhostSample
import com.Vantavu6.Sevlod.domain.model.RunResult
import com.Vantavu6.Sevlod.domain.model.RunVerdict
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository

class SaveRunResultUseCase(private val repository: ProgressRepository) {

    operator fun invoke(
        districtId: String,
        modeId: String,
        verdict: RunVerdict,
        score: Int,
        chain: Int,
        tokens: Int,
        distanceDp: Int,
        ghost: List<GhostSample>
    ): RunResult {
        val record = repository.commitRun(districtId, score, chain, tokens, ghost)
        return RunResult(
            districtId = districtId,
            modeId = modeId,
            verdict = verdict,
            score = score,
            bestChain = chain,
            tokens = tokens,
            distanceDp = distanceDp,
            newRecord = record
        )
    }
}
