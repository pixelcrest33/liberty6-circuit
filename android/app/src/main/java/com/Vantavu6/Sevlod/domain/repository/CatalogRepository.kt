package com.Vantavu6.Sevlod.domain.repository

import com.Vantavu6.Sevlod.domain.model.District
import com.Vantavu6.Sevlod.domain.model.TuningPart
import com.Vantavu6.Sevlod.domain.model.Weather

interface CatalogRepository {

    fun districts(): List<District>

    fun weathers(): List<Weather>

    fun parts(): List<TuningPart>

    fun districtOrDefault(id: String): District

    fun weatherOrDefault(id: String): Weather
}
