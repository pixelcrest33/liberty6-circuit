package com.Vantavu6.Sevlod.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Vantavu6.Sevlod.presentation.app.AppViewModel
import com.Vantavu6.Sevlod.presentation.district.DistrictSelectViewModel
import com.Vantavu6.Sevlod.presentation.game.GameViewModel
import com.Vantavu6.Sevlod.presentation.garage.GarageViewModel
import com.Vantavu6.Sevlod.presentation.gameover.GameOverViewModel
import com.Vantavu6.Sevlod.presentation.howtoplay.HowToPlayViewModel
import com.Vantavu6.Sevlod.presentation.menu.MenuViewModel
import com.Vantavu6.Sevlod.presentation.splash.SplashViewModel

class AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(ServiceLocator.progress(), ServiceLocator.catalog())

            modelClass.isAssignableFrom(DistrictSelectViewModel::class.java) ->
                DistrictSelectViewModel(ServiceLocator.progress(), ServiceLocator.catalog())

            modelClass.isAssignableFrom(GarageViewModel::class.java) ->
                GarageViewModel(ServiceLocator.progress(), ServiceLocator.catalog())

            modelClass.isAssignableFrom(HowToPlayViewModel::class.java) ->
                HowToPlayViewModel()

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(ServiceLocator.progress())

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    ServiceLocator.progress(),
                    ServiceLocator.catalog(),
                    ServiceLocator.generateRoute,
                    ServiceLocator.applyTuning,
                    ServiceLocator.scoreChain,
                    ServiceLocator.saveRunResult()
                )

            else -> error("Unknown ViewModel ${modelClass.name}")
        }
        return created as T
    }
}
