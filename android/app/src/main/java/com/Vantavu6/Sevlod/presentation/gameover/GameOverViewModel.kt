package com.Vantavu6.Sevlod.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val totalTokens: Int = 0,
    val bestScore: Int = 0,
    val bestChain: Int = 0
)

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.update {
                    it.copy(
                        totalTokens = progress.totalTokens,
                        bestScore = progress.bestScore,
                        bestChain = progress.bestChain
                    )
                }
            }
        }
    }
}
