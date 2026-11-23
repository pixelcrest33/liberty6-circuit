package com.Vantavu6.Sevlod.data.sample

import com.Vantavu6.Sevlod.domain.model.District
import com.Vantavu6.Sevlod.domain.model.TuningPart
import com.Vantavu6.Sevlod.domain.model.Weather

object SampleData {

    const val DISTRICT_HARBOUR = "harbour_mile"
    const val DISTRICT_CAUSEWAY = "neon_causeway"
    const val DISTRICT_HEIGHTS = "dawn_heights"

    const val WEATHER_CLEAR = "clear"
    const val WEATHER_FOG = "sea_fog"
    const val WEATHER_RAIN = "rain_squall"

    const val PART_GRIP = "grip_kit"
    const val PART_HEADLIGHT = "headlight_array"
    const val PART_NITRO = "nitro_cell"

    val districts: List<District> = listOf(
        District(
            id = DISTRICT_HARBOUR,
            index = 0,
            unlockScore = 0,
            gateSpacingDp = 300f,
            coneDensity = 1.0f,
            startSpeedDp = 150f,
            endSpeedDp = 200f,
            lengthDp = 11000f
        ),
        District(
            id = DISTRICT_CAUSEWAY,
            index = 1,
            unlockScore = 4000,
            gateSpacingDp = 250f,
            coneDensity = 1.4f,
            startSpeedDp = 175f,
            endSpeedDp = 230f,
            lengthDp = 13000f
        ),
        District(
            id = DISTRICT_HEIGHTS,
            index = 2,
            unlockScore = 9000,
            gateSpacingDp = 210f,
            coneDensity = 1.8f,
            startSpeedDp = 200f,
            endSpeedDp = 270f,
            lengthDp = 15000f
        )
    )

    val weathers: List<Weather> = listOf(
        Weather(id = WEATHER_CLEAR, laneChangeFactor = 1.0f, fadeFactor = 1.0f),
        Weather(id = WEATHER_FOG, laneChangeFactor = 1.0f, fadeFactor = 0.65f),
        Weather(id = WEATHER_RAIN, laneChangeFactor = 1.4f, fadeFactor = 1.0f)
    )

    val parts: List<TuningPart> = listOf(
        TuningPart(id = PART_GRIP, maxLevel = 3, costs = listOf(120, 260, 520)),
        TuningPart(id = PART_HEADLIGHT, maxLevel = 3, costs = listOf(100, 220, 460)),
        TuningPart(id = PART_NITRO, maxLevel = 3, costs = listOf(150, 320, 640))
    )
}
