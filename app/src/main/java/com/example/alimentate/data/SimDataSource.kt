package com.example.alimentate.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

/**
 * Fuente simulada:
 * - Genera valores aleatorios tipo refri cada vez que la llamas
 * - También guarda histórico
 */
class SimDataSource : SensorDataSource {

    private val last = MutableStateFlow<SensorReading?>(null)
    private val history = MutableStateFlow<List<SensorReading>>(emptyList())

    init {
        // inicializamos con una primera lectura random
        pushRandom()
    }

    // Generar nueva lectura random (la usaremos en MLFragment cada 2s)
    fun pushRandom() {
        val r = SensorReading(
            tempC = Random.nextFloat() * 10f + 5f,     // 5-15 °C
            humiPct = Random.nextFloat() * 30f + 70f,  // 70-100 %
            gasRaw = Random.nextInt(1200, 3200),       // MQ-4 "nivel gas"
            tsUnix = System.currentTimeMillis() / 1000
        )
        last.value = r
        history.value = (history.value + r).takeLast(50)
    }

    // Para cuando quieras sliders/manual en la demo
    fun pushManual(temp: Float, hum: Float, gas: Int) {
        val r = SensorReading(
            tempC = temp,
            humiPct = hum,
            gasRaw = gas,
            tsUnix = System.currentTimeMillis() / 1000
        )
        last.value = r
        history.value = (history.value + r).takeLast(50)
    }

    override fun lastReadingFlow(): Flow<SensorReading?> = last.asStateFlow()

    override fun historyFlow(limit: Int): Flow<List<SensorReading>> = history.asStateFlow()
}
