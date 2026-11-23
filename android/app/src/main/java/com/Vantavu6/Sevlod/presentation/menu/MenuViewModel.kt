package com.Vantavu6.Sevlod.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vantavu6.Sevlod.domain.repository.CatalogRepository
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val district = catalogRepository.districtOrDefault(progress.lastDistrictId)
                val weather = catalogRepository.weatherOrDefault(progress.lastWeatherId)
                _state.update {
                    it.copy(
                        tokens = progress.totalTokens,
                        bestScore = progress.bestScore,
                        bestChain = progress.bestChain,
                        districtId = district.id,
                        weatherId = weather.id
                    )
                }
            }
        }
    }
}
