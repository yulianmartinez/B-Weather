package com.bold.core.common.util

import kotlin.math.roundToInt

object TemperatureUtils {
    fun formatTemperature(celsiusTemp: Double, useCelsius: Boolean): String {
        val tempToDisplay = if (useCelsius) {
            celsiusTemp
        } else {
            (celsiusTemp * 9 / 5) + 32
        }
        val unit = if (useCelsius) "°C" else "°F"
        return "${tempToDisplay.roundToInt()}$unit"
    }
}
