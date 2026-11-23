package com.Vantavu6.Sevlod.presentation.app

import androidx.lifecycle.ViewModel
import com.Vantavu6.Sevlod.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = mutableListOf<Screen>()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) {
            backStack.add(current)
        }
        _screen.value = target
    }

    fun resetTo(target: Screen) {
        backStack.clear()
        _screen.value = target
    }

    fun restartRun() {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.Game
    }

    fun back(): Boolean {
        if (backStack.isEmpty()) return false
        val index = backStack.lastIndex
        val previous = backStack[index]
        backStack.removeAt(index)
        _screen.value = previous
        return true
    }
}
