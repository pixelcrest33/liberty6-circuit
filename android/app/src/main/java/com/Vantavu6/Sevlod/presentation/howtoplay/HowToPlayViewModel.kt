package com.Vantavu6.Sevlod.presentation.howtoplay

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class HowToStep {
    Lanes,
    Gates,
    Tokens,
    Bank
}

data class HowToPlayUiState(
    val steps: List<HowToStep> = HowToStep.entries.toList()
)

class HowToPlayViewModel : ViewModel() {

    private val _state = MutableStateFlow(HowToPlayUiState())
    val state: StateFlow<HowToPlayUiState> = _state.asStateFlow()
}
