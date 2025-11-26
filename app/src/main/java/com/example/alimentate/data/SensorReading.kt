package com.example.alimentate.data



data class SensorReading(
    val tempC: Float,
    val humiPct: Float,
    val gasRaw: Int,
    val tsUnix: Long
)