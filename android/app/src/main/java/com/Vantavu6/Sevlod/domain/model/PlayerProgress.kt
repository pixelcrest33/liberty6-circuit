package com.Vantavu6.Sevlod.domain.model

data class PlayerProgress(
    val totalTokens: Int = 0,
    val bestScores: Map<String, Int> = emptyMap(),
    val bestChain: Int = 0,
    val partLevels: Map<String, Int> = emptyMap(),
    val ghosts: Map<String, List<GhostSample>> = emptyMap(),
    val lastDistrictId: String = "",
    val lastWeatherId: String = "",
    val lastModeId: String = "",
    val ghostEnabled: Boolean = true,
    val controlSideRight: Boolean = false,
    val attempts: Int = 0
) {
    val bestScore: Int get() = bestScores.values.maxOrNull() ?: 0

    fun bestFor(districtId: String): Int = bestScores[districtId] ?: 0

    fun levelOf(partId: String): Int = partLevels[partId] ?: 0

    fun ghostFor(districtId: String): List<GhostSample> = ghosts[districtId] ?: emptyList()
}
