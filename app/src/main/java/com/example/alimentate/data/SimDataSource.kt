package com.example.alimentate.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class SimDataSource : SensorDataSource {

    private val last = MutableStateFlow<SensorReading?>(null)
    private val _history = mutableListOf<SensorReading>()
    private val historyFlowInternal = MutableStateFlow<List<SensorReading>>(emptyList())

    init {
        pushRandom()
    }

    fun pushRandom() {
        val r = SensorReading(
            tempC = Random.nextFloat() * 10f + 5f,    // 5-15 °C
            humiPct = Random.nextFloat() * 30f + 70f, // 70-100 %
            gasRaw = Random.nextInt(1200, 3200),      // MQ-4 "nivel gas"
            tsUnix = System.currentTimeMillis() / 1000
        )
        pushReading(r)
    }

    fun pushManual(temp: Float, hum: Float, gas: Int) {
        val r = SensorReading(temp, hum, gas, System.currentTimeMillis() / 1000)
        pushReading(r)
    }

    private fun pushReading(reading: SensorReading) {
        last.value = reading
        _history.add(reading)
        if (_history.size > 50) {
            _history.removeAt(0) // Mantener solo las últimas 50 lecturas
        }
        historyFlowInternal.value = _history.toList()
    }

    override fun lastReadingFlow(): Flow<SensorReading?> = last.asStateFlow()

    override fun historyFlow(limit: Int): Flow<List<SensorReading>> =
        historyFlowInternal.asStateFlow().map { it.takeLast(limit) }
}
