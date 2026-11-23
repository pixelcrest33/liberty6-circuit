package com.Vantavu6.Sevlod.presentation.garage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Vantavu6.Sevlod.domain.repository.CatalogRepository
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GarageViewModel(
    private val progressRepository: ProgressRepository,
    private val catalogRepository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GarageUiState())
    val state: StateFlow<GarageUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.update { current ->
                    current.copy(
                        tokens = progress.totalTokens,
                        rows = catalogRepository.parts().map { part ->
                            val level = progress.levelOf(part.id)
                            val cost = part.costForLevel(level)
                            TuningRowState(
                                id = part.id,
                                level = level,
                                maxLevel = part.maxLevel,
                                nextCost = cost,
                                affordable = level < part.maxLevel && progress.totalTokens >= cost
                            )
                        }
                    )
                }
            }
        }
    }

    fun install(partId: String) {
        val row = _state.value.rows.firstOrNull { it.id == partId } ?: return
        if (row.maxed || !row.affordable) return
        if (!progressRepository.spendTokens(row.nextCost)) return
        progressRepository.setPartLevel(partId, row.level + 1)
        _state.update { it.copy(installedPartId = partId) }
        viewModelScope.launch {
            delay(TOAST_MS)
            _state.update { if (it.installedPartId == partId) it.copy(installedPartId = "") else it }
        }
    }

    private companion object {
        const val TOAST_MS = 1600L
    }
}
