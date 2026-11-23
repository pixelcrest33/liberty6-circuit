package com.Vantavu6.Sevlod.presentation.district

import com.Vantavu6.Sevlod.domain.model.RunMode

data class DistrictCardState(
    val id: String,
    val unlockScore: Int,
    val unlocked: Boolean,
    val best: Int,
    val selected: Boolean
)

data class DistrictSelectUiState(
    val districts: List<DistrictCardState> = emptyList(),
    val weatherIds: List<String> = emptyList(),
    val selectedWeatherId: String = "",
    val selectedMode: RunMode = RunMode.DawnRun,
    val ghostEnabled: Boolean = true,
    val controlSideRight: Boolean = false,
    val tokens: Int = 0
)
