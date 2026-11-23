package com.Vantavu6.Sevlod.core.di

import android.content.Context
import com.Vantavu6.Sevlod.data.local.GamePreferences
import com.Vantavu6.Sevlod.data.repository.CatalogRepositoryImpl
import com.Vantavu6.Sevlod.data.repository.ProgressRepositoryImpl
import com.Vantavu6.Sevlod.domain.repository.CatalogRepository
import com.Vantavu6.Sevlod.domain.repository.ProgressRepository
import com.Vantavu6.Sevlod.domain.usecase.ApplyTuningUseCase
import com.Vantavu6.Sevlod.domain.usecase.GenerateRouteUseCase
import com.Vantavu6.Sevlod.domain.usecase.SaveRunResultUseCase
import com.Vantavu6.Sevlod.domain.usecase.ScoreChainUseCase

object ServiceLocator {

    private var progressRepository: ProgressRepository? = null
    private var catalogRepository: CatalogRepository? = null

    val applyTuning = ApplyTuningUseCase()
    val generateRoute = GenerateRouteUseCase()
    val scoreChain = ScoreChainUseCase()

    fun init(context: Context) {
        if (progressRepository == null) {
            progressRepository = ProgressRepositoryImpl(GamePreferences(context.applicationContext))
        }
        if (catalogRepository == null) {
            catalogRepository = CatalogRepositoryImpl()
        }
    }

    fun progress(): ProgressRepository =
        progressRepository ?: error(NOT_READY)

    fun catalog(): CatalogRepository =
        catalogRepository ?: error(NOT_READY)

    fun saveRunResult(): SaveRunResultUseCase = SaveRunResultUseCase(progress())

    private const val NOT_READY = "ServiceLocator.init was not called"
}
