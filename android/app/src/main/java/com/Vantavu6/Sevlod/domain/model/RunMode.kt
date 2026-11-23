package com.Vantavu6.Sevlod.domain.model

enum class RunMode(val id: String) {
    DawnRun("dawn_run"),
    TimeTrial("time_trial");

    companion object {
        fun fromId(id: String): RunMode = entries.firstOrNull { it.id == id } ?: DawnRun
    }
}
