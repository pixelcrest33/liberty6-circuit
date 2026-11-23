package com.Vantavu6.Sevlod.presentation.common

import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.data.sample.SampleData
import com.Vantavu6.Sevlod.domain.model.RunMode
import com.Vantavu6.Sevlod.domain.model.RunVerdict

object CatalogLabels {

    fun districtName(id: String): Int = when (id) {
        SampleData.DISTRICT_CAUSEWAY -> R.string.district_causeway
        SampleData.DISTRICT_HEIGHTS -> R.string.district_heights
        else -> R.string.district_harbour
    }

    fun districtBrief(id: String): Int = when (id) {
        SampleData.DISTRICT_CAUSEWAY -> R.string.district_causeway_brief
        SampleData.DISTRICT_HEIGHTS -> R.string.district_heights_brief
        else -> R.string.district_harbour_brief
    }

    fun weatherName(id: String): Int = when (id) {
        SampleData.WEATHER_FOG -> R.string.weather_fog
        SampleData.WEATHER_RAIN -> R.string.weather_rain
        else -> R.string.weather_clear
    }

    fun modeName(mode: RunMode): Int = when (mode) {
        RunMode.TimeTrial -> R.string.mode_time_trial
        RunMode.DawnRun -> R.string.mode_dawn_run
    }

    fun partName(id: String): Int = when (id) {
        SampleData.PART_HEADLIGHT -> R.string.part_headlight
        SampleData.PART_NITRO -> R.string.part_nitro
        else -> R.string.part_grip
    }

    fun partEffect(id: String): Int = when (id) {
        SampleData.PART_HEADLIGHT -> R.string.part_headlight_effect
        SampleData.PART_NITRO -> R.string.part_nitro_effect
        else -> R.string.part_grip_effect
    }

    fun verdictTitle(verdict: RunVerdict): Int = when (verdict) {
        RunVerdict.Crashed -> R.string.verdict_crashed
        RunVerdict.DawnCleared -> R.string.verdict_dawn_cleared
        RunVerdict.SunCaught -> R.string.verdict_sun_caught
        RunVerdict.ClockOut -> R.string.verdict_clock_out
    }

    fun verdictSubtitle(verdict: RunVerdict): Int = when (verdict) {
        RunVerdict.Crashed -> R.string.verdict_crashed_sub
        RunVerdict.DawnCleared -> R.string.verdict_dawn_cleared_sub
        RunVerdict.SunCaught -> R.string.verdict_sun_caught_sub
        RunVerdict.ClockOut -> R.string.verdict_clock_out_sub
    }
}
