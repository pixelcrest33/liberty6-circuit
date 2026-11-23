package com.Vantavu6.Sevlod.presentation.menu

import com.Vantavu6.Sevlod.data.sample.SampleData

data class MenuUiState(
    val tokens: Int = 0,
    val bestScore: Int = 0,
    val bestChain: Int = 0,
    val districtId: String = SampleData.DISTRICT_HARBOUR,
    val weatherId: String = SampleData.WEATHER_CLEAR
)
