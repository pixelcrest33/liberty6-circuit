package com.Vantavu6.Sevlod.data.repository

import com.Vantavu6.Sevlod.data.sample.SampleData
import com.Vantavu6.Sevlod.domain.model.District
import com.Vantavu6.Sevlod.domain.model.TuningPart
import com.Vantavu6.Sevlod.domain.model.Weather
import com.Vantavu6.Sevlod.domain.repository.CatalogRepository

class CatalogRepositoryImpl : CatalogRepository {

    override fun districts(): List<District> = SampleData.districts

    override fun weathers(): List<Weather> = SampleData.weathers

    override fun parts(): List<TuningPart> = SampleData.parts

    override fun districtOrDefault(id: String): District =
        SampleData.districts.firstOrNull { it.id == id } ?: SampleData.districts.first()

    override fun weatherOrDefault(id: String): Weather =
        SampleData.weathers.firstOrNull { it.id == id } ?: SampleData.weathers.first()
}
