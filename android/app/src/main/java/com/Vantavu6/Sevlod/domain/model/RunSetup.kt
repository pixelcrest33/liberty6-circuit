package com.Vantavu6.Sevlod.domain.model

data class RunSetup(
    val district: District,
    val weather: Weather,
    val mode: RunMode,
    val gripLevel: Int,
    val headlightLevel: Int,
    val nitroLevel: Int,
    val ghostEnabled: Boolean,
    val controlSideRight: Boolean,
    val attempt: Int
)
