package com.Vantavu6.Sevlod.presentation.app

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vantavu6.Sevlod.core.di.AppViewModelFactory
import com.Vantavu6.Sevlod.core.navigation.Screen
import com.Vantavu6.Sevlod.core.theme.BgDeep
import com.Vantavu6.Sevlod.presentation.district.DistrictSelectRoute
import com.Vantavu6.Sevlod.presentation.game.GameRoute
import com.Vantavu6.Sevlod.presentation.gameover.GameOverRoute
import com.Vantavu6.Sevlod.presentation.garage.GarageRoute
import com.Vantavu6.Sevlod.presentation.howtoplay.HowToPlayRoute
import com.Vantavu6.Sevlod.presentation.menu.MenuRoute
import com.Vantavu6.Sevlod.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val factory = remember { AppViewModelFactory() }
    val appViewModel: AppViewModel = viewModel(factory = factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (!appViewModel.back()) {
            (context as? Activity)?.finish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = { fadeIn(tween(420)) togetherWith fadeOut(tween(300)) },
            label = "screen_host"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashRoute(
                    factory = factory,
                    onFinished = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.Menu -> MenuRoute(
                    factory = factory,
                    onPlay = { appViewModel.navigate(Screen.Game) },
                    onDistrictSelect = { appViewModel.navigate(Screen.DistrictSelect) },
                    onGarage = { appViewModel.navigate(Screen.Garage) },
                    onHowToPlay = { appViewModel.navigate(Screen.HowToPlay) }
                )

                Screen.Game -> GameRoute(
                    factory = factory,
                    onFinished = { result -> appViewModel.navigate(Screen.GameOver(result)) },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.GameOver -> GameOverRoute(
                    factory = factory,
                    result = target.result,
                    onPlayAgain = { appViewModel.restartRun() },
                    onDistrictSelect = { appViewModel.resetTo(Screen.DistrictSelect) },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.DistrictSelect -> DistrictSelectRoute(
                    factory = factory,
                    onStartRun = { appViewModel.restartRun() },
                    onBack = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.Garage -> GarageRoute(
                    factory = factory,
                    onBack = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.HowToPlay -> HowToPlayRoute(
                    factory = factory,
                    onPlay = { appViewModel.restartRun() },
                    onBack = { appViewModel.resetTo(Screen.Menu) }
                )
            }
        }
    }
}
