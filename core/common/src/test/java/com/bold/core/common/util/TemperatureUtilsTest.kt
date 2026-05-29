package com.bold.core.common.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TemperatureUtilsTest {

    @Test
    fun `formatTemperature should return Celsius string when useCelsius is true`() {
        val temp = 20.4
        val result = TemperatureUtils.formatTemperature(temp, useCelsius = true)
        
        // 20.4 rounds to 20
        assertEquals("20°C", result)
    }

    @Test
    fun `formatTemperature should convert and return Fahrenheit string when useCelsius is false`() {
        val temp = 20.0
        // (20 * 9/5) + 32 = 68
        val result = TemperatureUtils.formatTemperature(temp, useCelsius = false)
        
        assertEquals("68°F", result)
    }

    @Test
    fun `formatTemperature should round correctly`() {
        val temp1 = 20.5
        val result1 = TemperatureUtils.formatTemperature(temp1, useCelsius = true)
        assertEquals("21°C", result1)

        val temp2 = 20.4
        val result2 = TemperatureUtils.formatTemperature(temp2, useCelsius = true)
        assertEquals("20°C", result2)
    }
}
