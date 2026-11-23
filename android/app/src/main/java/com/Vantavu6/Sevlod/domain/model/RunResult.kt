package com.Vantavu6.Sevlod.domain.model

enum class RunVerdict {
    Crashed,
    DawnCleared,
    SunCaught,
    ClockOut
}

data class RunResult(
    val districtId: String,
    val modeId: String,
    val verdict: RunVerdict,
    val score: Int,
    val bestChain: Int,
    val tokens: Int,
    val distanceDp: Int,
    val newRecord: Boolean
)
