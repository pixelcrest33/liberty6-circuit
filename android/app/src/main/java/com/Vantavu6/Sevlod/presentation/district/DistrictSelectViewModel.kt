package com.Vantavu6.Sevlod.presentation.district

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vantavu6.Sevlod.domain.model.RunMode
import com.Vantavu6.Sevlod.domain.repository.CatalogRepository
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DistrictSelectViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DistrictSelectUiState())
    val state: StateFlow<DistrictSelectUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val selected = catalogRepository.districtOrDefault(progress.lastDistrictId)
                val best = progress.bestScore
                _state.value = DistrictSelectUiState(
                    districts = catalogRepository.districts().map { district ->
                        DistrictCardState(
                            id = district.id,
                            unlockScore = district.unlockScore,
                            unlocked = best >= district.unlockScore,
                            best = progress.bestFor(district.id),
                            selected = district.id == selected.id
                        )
                    },
                    weatherIds = catalogRepository.weathers().map { it.id },
                    selectedWeatherId = catalogRepository.weatherOrDefault(progress.lastWeatherId).id,
                    selectedMode = RunMode.fromId(progress.lastModeId),
                    ghostEnabled = progress.ghostEnabled,
                    controlSideRight = progress.controlSideRight,
                    tokens = progress.totalTokens
                )
            }
        }
    }

    fun selectDistrict(id: String) {
        val card = _state.value.districts.firstOrNull { it.id == id } ?: return
        if (!card.unlocked) return
        progressRepository.selectDistrict(id)
    }

    fun selectWeather(id: String) = progressRepository.selectWeather(id)

    fun selectMode(mode: RunMode) = progressRepository.selectMode(mode.id)

    fun toggleGhost() = progressRepository.setGhostEnabled(!_state.value.ghostEnabled)

    fun toggleControlSide() = progressRepository.setControlSideRight(!_state.value.controlSideRight)
}
