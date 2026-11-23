package com.Vantavu6.Sevlod.core.navigation

import com.Vantavu6.Sevlod.domain.model.RunResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Game : Screen

    data class GameOver(val result: RunResult) : Screen

    data object DistrictSelect : Screen

    data object Garage : Screen

    data object HowToPlay : Screen
}
